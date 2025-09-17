package com.example.fitlogtimer.data.repository

import com.example.fitlogtimer.data.local.JsonDataManager
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.WorkoutType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    private val jsonDataManager: JsonDataManager
) {
    //charge la liste d'exercices pour le formulaire
    suspend fun getExercises(): List<Exercise> = withContext(Dispatchers.IO) {
        jsonDataManager.loadExercisesFromJson()
    }

    //charge la liste de types de workouts pour le formulaire
    suspend fun getWorkoutTypes(): List<WorkoutType> = withContext(Dispatchers.IO) {
        jsonDataManager.loadWorkoutTypesFromJson()
    }

}