package com.example.fitlogtimer.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.fitlogtimer.data.local.JsonDataManager
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseSet
import com.example.fitlogtimer.data.model.WorkoutType
import com.example.fitlogtimer.data.repository.DataRepository
import com.example.fitlogtimer.data.repository.DriveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.fitlogtimer.data.remote.drive.GoogleDriveManager

data class ExerciseSetFormState(
    val selectedExerciseId: Int = -1,
    val reps: String = "",
    val weight: String = "",
    val exercises: List<Exercise> = emptyList(),
    val exerciseSets: List<ExerciseSet> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val isFormValid: Boolean
        get() = selectedExerciseId != -1 &&
                reps.isNotBlank() && reps.toIntOrNull() != null &&
                weight.isNotBlank() && weight.toDoubleOrNull() != null
}



class ExerciseSetViewModel(
    app: Application,
    private val repository: DataRepository,
    private val driveRepository: DriveRepository,
    private val jsonDataManager: JsonDataManager
) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(ExerciseSetFormState())
    val uiState: StateFlow<ExerciseSetFormState> = _uiState.asStateFlow()

    // État d'exportation
    sealed class ExportStatus {
        object Idle : ExportStatus()
        data class Success(val fileId: String) : ExportStatus()
        data class Error(val message: String? = null, val exception: Throwable? = null) : ExportStatus()
    }
    private val _exportStatus = MutableStateFlow<ExportStatus>(ExportStatus.Idle)
    val exportStatus: StateFlow<ExportStatus> = _exportStatus

    private val driveManager = GoogleDriveManager(app.applicationContext)

    private val context: Context = app.applicationContext

    var bodyWeight by androidx.compose.runtime.mutableStateOf("")
        private set

    var showBodyWeightDialog by androidx.compose.runtime.mutableStateOf(false)
        private set

    var selectedWorkoutType by androidx.compose.runtime.mutableStateOf<String?>(null)
        private set

    var showWorkoutTypeSelector by androidx.compose.runtime.mutableStateOf(false)
        private set

    var workoutTypes by androidx.compose.runtime.mutableStateOf<List<WorkoutType>>(emptyList())
        private set

    companion object {
        const val FITLOG_FOLDER_ID = "1Ukuk_217ZUkXb3A75G2Sedi4Q5t8tMKa"
    }

    init {
        loadExercises()
        loadWorkoutTypes()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            try {
                val exercises = repository.getExercises()
                _uiState.value = _uiState.value.copy(
                    exercises = exercises,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erreur lors du chargement: ${e.message}"
                )
            }
        }
    }

    fun updateSelectedExercise(exerciseId: Int) {
        val currentState = _uiState.value
        val exercise = currentState.exercises.find { it.id == exerciseId }

        _uiState.value = currentState.copy(
            selectedExerciseId = exerciseId,
            reps = exercise?.defaultReps?.toString() ?: currentState.reps,
            weight = exercise?.defaultWeight?.toString() ?: currentState.weight
        )
    }

    fun updateReps(reps: String) {
        _uiState.value = _uiState.value.copy(reps = reps)
    }

    fun updateWeight(newWeight: String) {
        _uiState.value = _uiState.value.copy(weight = newWeight)
    }

    fun addExerciseSet() {
        val currentState = _uiState.value
        val selectedExercise = currentState.exercises.find { it.id == currentState.selectedExerciseId }

        if (selectedExercise != null) {
            val newSet = ExerciseSet(
                exerciseId = selectedExercise.id,
                reps = currentState.reps.toIntOrNull() ?: selectedExercise.defaultReps,
                weight = currentState.weight.toDoubleOrNull() ?: selectedExercise.defaultWeight
            )

            val updatedSets = currentState.exerciseSets + newSet

            _uiState.value = currentState.copy(
                exerciseSets = updatedSets,
                selectedExerciseId = selectedExercise.id,
                reps = newSet.reps.toString(),
                weight = newSet.weight.toString()
            )
        }
    }

    private fun loadWorkoutTypes() {
        viewModelScope.launch {
            try {
                val types = repository.getWorkoutTypes()
                workoutTypes = types
            } catch (e: Exception) {
                Log.e("ViewModel", "Erreur lors du chargement", e)
            }
        }
    }

    fun updateWorkoutType(type: String?) {
        selectedWorkoutType = type
    }

    fun clearWorkout() {
        _uiState.value = _uiState.value.copy(exerciseSets = emptyList())
    }

    fun updateBodyWeight(value: String) {
        bodyWeight = value
    }

    fun toggleBodyWeightDialog() {
        showBodyWeightDialog = !showBodyWeightDialog
    }

    fun toggleWorkoutTypeSelector() {
        showWorkoutTypeSelector = !showWorkoutTypeSelector
    }

    fun exportWorkoutToJson(): String {
        return jsonDataManager.exportToJson(
            exerciseSets = _uiState.value.exerciseSets,
            date = LocalDate.now().toString(),
            workoutType = selectedWorkoutType,
            bodyWeight = bodyWeight.toDoubleOrNull()
        )
    }

    fun exportToDrive(jsonContent: String, fileName: String) {
        viewModelScope.launch {
            _exportStatus.value = ExportStatus.Idle
            Log.d("DriveDebug", "4. ViewModel - Début export: $fileName")

            try {
                Log.d("DriveDebug", "4.1 ViewModel - Appel repository...")
                val result = driveRepository.uploadWorkoutToDrive(fileName, jsonContent)

                if (result.isSuccess) {
                    val fileId = result.getOrNull() ?: ""
                    Log.d("DriveDebug", "✅ 4.2 ViewModel - Succès final, fileId: $fileId")
                    _exportStatus.value = ExportStatus.Success(fileId)
                } else {
                    val exception = result.exceptionOrNull() ?: Exception("Erreur inconnue")
                    Log.e("DriveDebug", "❌ 4.2 ViewModel - Échec final: ${exception.message}")

                    _exportStatus.value = ExportStatus.Error(
                        message = exception.message,
                        exception = exception
                    )

                }

            } catch (e: Exception) {
                Log.e("DriveDebug", "❌ 4.3 ViewModel - Exception non gérée: ${e.message}")

                _exportStatus.value = ExportStatus.Error(
                    message = e.message,
                    exception = e
                )

            }
        }
    }

    fun resetExportStatus() {
        _exportStatus.value = ExportStatus.Idle
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

