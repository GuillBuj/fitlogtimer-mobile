package com.example.fitlogtimer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseSet
import com.example.fitlogtimer.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun updateWeight(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    fun addExerciseSet() {
        val currentState = _uiState.value
        if (currentState.isFormValid) {
            val exerciseSet = ExerciseSet(
                exerciseId = currentState.selectedExerciseId,
                reps = currentState.reps.toInt(),
                weight = currentState.weight.toDouble()
            )

            _uiState.value = currentState.copy(
                exerciseSets = currentState.exerciseSets + exerciseSet,
                reps = "",
                weight = "",
                selectedExerciseId = -1
            )
        }
    }

    fun clearWorkout() {
        _uiState.value = _uiState.value.copy(exerciseSets = emptyList())
    }

    fun exportToJson(): String {
        return repository.exportExerciseSets(_uiState.value.exerciseSets)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}