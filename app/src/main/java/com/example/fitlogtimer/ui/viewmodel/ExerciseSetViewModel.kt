package com.example.fitlogtimer.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseSet
import com.example.fitlogtimer.data.model.WorkoutExport
import com.example.fitlogtimer.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

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

class ExerciseSetViewModel(private val repository: DataRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseSetFormState())
    val uiState: StateFlow<ExerciseSetFormState> = _uiState.asStateFlow()

    var bodyWeight by mutableStateOf("")
        private set

    var showBodyWeightDialog by mutableStateOf(false)
        private set

    init {
        loadExercises()
    }

    private fun loadExercises() {
        viewModelScope.launch {
            try {
                val exercises = withContext(Dispatchers.IO) {
                    repository.getExercises()
                }
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

            //recopie le dernier set pour mettre en valeur par defaut
            _uiState.value = currentState.copy(
                exerciseSets = updatedSets,
                selectedExerciseId = selectedExercise.id,
                reps = newSet.reps.toString(),
                weight = newSet.weight.toString()
            )
        }
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

    fun exportWorkoutToJson(): String {
        val bodyWeightValue = bodyWeight.toDoubleOrNull()

        val workoutExport = WorkoutExport(
            date = LocalDate.now().toString(),
            bodyWeight = bodyWeightValue,
            exerciseSets = _uiState.value.exerciseSets
        )

        return Json.encodeToString(workoutExport)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}