package com.example.fitlogtimer.data.repository

import com.example.fitlogtimer.data.manager.JsonDataManager
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    private val jsonDataManager: JsonDataManager
) {

    suspend fun getExercises(): List<Exercise> = withContext(Dispatchers.IO) {
        jsonDataManager.loadFromJson()
    }

    fun exportExerciseSets(exerciseSets: List<ExerciseSet>): String {
        return jsonDataManager.exportToJson(exerciseSets)
    }
}