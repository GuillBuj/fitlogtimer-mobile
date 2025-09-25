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
    val bands: String = "",
    val durationS: String = "",
    val distance: String = "",
    val exercises: List<Exercise> = emptyList(),
    val exerciseSets: List<ExerciseSet> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val isFormValid: Boolean
        get() {
            if (selectedExerciseId == -1) return false
            return when (exercises.find { it.id == selectedExerciseId }?.type) {
                "FREE_WEIGHT" -> reps.isNotBlank() && reps.toIntOrNull() != null &&
                        weight.isNotBlank() && weight.toDoubleOrNull() != null

                "ELASTIC" -> reps.isNotBlank() && reps.toIntOrNull() != null &&
                        bands.isNotBlank()

                "ISOMETRIC" -> durationS.isNotBlank() && durationS.toIntOrNull() != null &&
                        weight.toDoubleOrNull() != null

                "BODYWEIGHT" -> reps.isNotBlank() && reps.toIntOrNull() != null

                "MOVEMENT" -> reps.isNotBlank() && reps.toIntOrNull() != null &&
                        distance.isNotBlank()

                else -> false
            }
        }
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
        viewModelScope.launch {
            val refreshResult = repository.refreshExercisesFromDrive()
            if (refreshResult.isFailure) {
                Log.e("DriveDebug", "⚠️ Impossible de rafraîchir exercises.json: ${refreshResult.exceptionOrNull()?.message}")
            }

            loadExercises()
            loadWorkoutTypes()
        }
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
            reps = exercise?.defaultReps?.toString().orEmpty(),
            weight = exercise?.defaultWeight?.toString().orEmpty(),
            bands = exercise?.defaultBands.orEmpty(),
            durationS = exercise?.defaultDurationS?.toString().orEmpty(),
            distance = exercise?.defaultDistance.orEmpty()
        )
    }

    fun updateReps(reps: String) {
        _uiState.value = _uiState.value.copy(reps = reps)
    }

    fun updateWeight(newWeight: String) {
        _uiState.value = _uiState.value.copy(weight = newWeight)
    }

    fun updateBands(bands: String) {
        _uiState.value = _uiState.value.copy(bands = bands)
    }

    fun updateDuration(duration: String) {
        _uiState.value = _uiState.value.copy(durationS = duration)
    }

    fun updateDistance(distance: String) {
        _uiState.value = _uiState.value.copy(distance = distance)
    }


    fun addExerciseSet() {
        val currentState = _uiState.value
        val selectedExercise = currentState.exercises.find { it.id == currentState.selectedExerciseId } ?: return

        val newSet = when (selectedExercise.type) {
            "FREE_WEIGHT" -> ExerciseSet(
                exerciseId = selectedExercise.id,
                repNumber = currentState.reps.toIntOrNull() ?: 0,
                weight = currentState.weight.toDoubleOrNull() ?: 0.0,
                bands = "",
                durationS = 0,
                distance = "",
                type = "FREE_WEIGHT"
            )

            "ELASTIC" -> ExerciseSet(
                exerciseId = selectedExercise.id,
                repNumber = currentState.reps.toIntOrNull() ?: 0,
                weight = 0.0,
                bands = currentState.bands,
                durationS = 0,
                distance = "",
                type = "ELASTIC"
            )

            "ISOMETRIC" -> ExerciseSet(
                exerciseId = selectedExercise.id,
                repNumber = currentState.reps.toIntOrNull() ?: 0,
                weight = currentState.weight.toDoubleOrNull() ?: 0.0,
                bands = "",
                durationS = currentState.durationS.toIntOrNull() ?: 0,
                distance = "",
                type = "ISOMETRIC"
            )

            "BODYWEIGHT" -> ExerciseSet(
                exerciseId = selectedExercise.id,
                repNumber = currentState.reps.toIntOrNull() ?: 0,
                weight = currentState.weight.toDoubleOrNull() ?: 0.0,
                bands = currentState.bands,
                durationS = 0,
                distance = "",
                type = "BODYWEIGHT"
            )

            "MOVEMENT" -> ExerciseSet(
                exerciseId = selectedExercise.id,
                repNumber = currentState.reps.toIntOrNull() ?: 0,
                weight = currentState.weight.toDoubleOrNull() ?: 0.0,
                bands = currentState.bands,
                durationS = 0,
                distance = currentState.distance,
                type = "MOVEMENT"
            )

            else -> return
        }

        _uiState.value = currentState.copy(
            exerciseSets = currentState.exerciseSets + newSet
        )
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

