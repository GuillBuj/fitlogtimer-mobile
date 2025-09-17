package com.example.fitlogtimer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitlogtimer.data.local.JsonDataManager
import com.example.fitlogtimer.data.repository.DataRepository
import com.example.fitlogtimer.data.repository.DriveRepository

class ExerciseSetViewModelFactory(
    private val repository: DataRepository,
    private val driveRepository: DriveRepository,
    private val jsonDataManager: JsonDataManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseSetViewModel::class.java)) {
            return ExerciseSetViewModel(repository, driveRepository, jsonDataManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}