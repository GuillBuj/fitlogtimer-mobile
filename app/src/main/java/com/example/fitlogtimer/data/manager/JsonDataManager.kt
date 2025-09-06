package com.example.fitlogtimer.data.manager

import android.content.Context
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseList
import com.example.fitlogtimer.data.model.ExerciseSet
import com.example.fitlogtimer.data.model.WorkoutExport
import com.google.gson.Gson
import java.io.BufferedReader

class JsonDataManager(private val context: Context) {

    fun loadFromJson(): List<Exercise> {
        val jsonString = context.assets.open("exercises.json")
            .bufferedReader().use(BufferedReader::readText)

        val exerciseList = Gson().fromJson(jsonString, ExerciseList::class.java)
        return exerciseList.exercises.sortedBy { it.position }
    }

    fun exportToJson(exerciseSets: List<ExerciseSet>): String {
        val workoutExport = WorkoutExport(exerciseSets = exerciseSets)
        return Gson().toJson(workoutExport)
    }
}