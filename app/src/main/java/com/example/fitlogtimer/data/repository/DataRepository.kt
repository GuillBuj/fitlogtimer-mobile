package com.example.fitlogtimer.data.repository

import com.example.fitlogtimer.data.manager.JsonDataManager
import com.example.fitlogtimer.data.model.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    private val jsonDataManager: JsonDataManager
) {
    //charge la liste d'exercices pour le formulaire
    suspend fun getExercises(): List<Exercise> = withContext(Dispatchers.IO) {
        jsonDataManager.loadFromJson()
    }

}