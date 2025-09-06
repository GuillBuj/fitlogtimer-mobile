package com.example.fitlogtimer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitlogtimer.data.repository.DataRepository

class ExerciseSetViewModelFactory(
    private val repository: DataRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseSetViewModel::class.java)) {
            return ExerciseSetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}