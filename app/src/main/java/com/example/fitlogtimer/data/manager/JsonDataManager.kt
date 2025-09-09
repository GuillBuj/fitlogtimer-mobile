package com.example.fitlogtimer.data.manager

import android.content.Context
import com.example.fitlogtimer.data.model.AppInputs
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseList
import com.example.fitlogtimer.data.model.ExerciseSet
import com.example.fitlogtimer.data.model.WorkoutExport
import com.example.fitlogtimer.data.model.WorkoutType
import com.example.fitlogtimer.data.model.WorkoutTypeList
import com.google.gson.Gson
import java.io.BufferedReader

class JsonDataManager(private val context: Context) {

    // Charge TOUTES les données d'un coup
    private fun loadAppInputs(): AppInputs {
        val jsonString = context.assets.open("exercises.json")
            .bufferedReader().use(BufferedReader::readText)
        return Gson().fromJson(jsonString, AppInputs::class.java)
    }

    // Charge seulement les exercices
    fun loadExercisesFromJson(): List<Exercise> {
        val AppInputs = loadAppInputs()
        return AppInputs.exercises.sortedBy { it.position }
    }

    // Charge seulement les types d'entraînement
    fun loadWorkoutTypesFromJson(): List<WorkoutType> {
        val AppInputs = loadAppInputs()
        return AppInputs.workoutTypes
    }

    fun exportToJson(exerciseSets: List<ExerciseSet>, date: String, bodyWeight: Double? = null, workoutType: String? = null): String {
        val workoutExport = WorkoutExport(
            exerciseSets = exerciseSets,
            bodyWeight = bodyWeight,
            workoutType = workoutType,
            date = date)
        return Gson().toJson(workoutExport)
    }
}