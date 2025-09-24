package com.example.fitlogtimer.data.local

import android.content.Context
import android.util.Log
import com.example.fitlogtimer.data.model.AppInputs
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseSet
import com.example.fitlogtimer.data.model.WorkoutExport
import com.example.fitlogtimer.data.model.WorkoutType
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import kotlin.math.log

class JsonDataManager(private val context: Context) {

    // Charge TOUTES les données d'un coup
    private fun loadAppInputs(): AppInputs {
        val jsonString = context.assets.open("exercises.json")
            .bufferedReader().use(BufferedReader::readText)
        return Gson().fromJson(jsonString, AppInputs::class.java)
    }

    // Charge seulement les exercices
    fun loadExercisesFromJson(): List<Exercise> {
        val file = File(context.filesDir, "exercises.json")
        val jsonString = if (file.exists()) {
            file.readText()
        } else {
            context.assets.open("exercises.json").bufferedReader().use { it.readText() }
        }
        val appInputs = Gson().fromJson(jsonString, AppInputs::class.java)
        return appInputs.exercises.sortedBy { it.position }
    }

    // Charge seulement les types d'entraînement
    fun loadWorkoutTypesFromJson(): List<WorkoutType> {
        val file = File(context.filesDir, "exercises.json")
        val jsonString: String
        if (file.exists()) {
            jsonString = file.readText()
            Log.d("JsonDataManager", "✅ exercises.json trouvé dans filesDir, taille=${jsonString.length}")
        } else {
            jsonString = context.assets.open("exercises.json")
                .bufferedReader().use { it.readText() }
            Log.d("JsonDataManager", "ℹ️ exercises.json fallback sur assets, taille=${jsonString.length}")
        }

        val appInputs = Gson().fromJson(jsonString, AppInputs::class.java)
        Log.d("JsonDataManager", "Nombre de workoutTypes parsés=${appInputs.workoutTypes.size}")
        return appInputs.workoutTypes
    }


    fun exportToJson(exerciseSets: List<ExerciseSet>, date: String, bodyWeight: Double? = null, workoutType: String? = null): String {
        val workoutExport = WorkoutExport(
            sets = exerciseSets,
            bodyWeight = bodyWeight,
            workoutType = workoutType?: "-",
            date = date
        )
        return Gson().toJson(workoutExport)
    }

    fun replaceExercisesJson(jsonContent: String) {
        val file = File(context.filesDir, "exercises.json")
        file.writeText(jsonContent)
    }
}