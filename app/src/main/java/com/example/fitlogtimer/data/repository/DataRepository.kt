package com.example.fitlogtimer.data.repository

import com.example.fitlogtimer.data.local.JsonDataManager
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.WorkoutType
import com.example.fitlogtimer.data.remote.drive.GoogleDriveManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    private val jsonDataManager: JsonDataManager,
    private val driveManager: GoogleDriveManager
) {

    suspend fun refreshExercisesFromDrive(): Result<Unit> = withContext(Dispatchers.IO) {
        val result = driveManager.downloadExercisesFile()
        if (result.isSuccess) {
            val jsonContent = result.getOrNull() ?: return@withContext Result.failure(Exception("Contenu vide"))
            jsonDataManager.replaceExercisesJson(jsonContent) // à créer juste en dessous
            Result.success(Unit)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Erreur inconnue"))
        }
    }

    //charge la liste d'exercices pour le formulaire
    suspend fun getExercises(): List<Exercise> = withContext(Dispatchers.IO) {
        jsonDataManager.loadExercisesFromJson()
    }

    //charge la liste de types de workouts pour le formulaire
    suspend fun getWorkoutTypes(): List<WorkoutType> = withContext(Dispatchers.IO) {
        jsonDataManager.loadWorkoutTypesFromJson()
    }

}