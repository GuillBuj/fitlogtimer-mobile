package com.example.fitlogtimer.data.json

import android.content.Context
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseList
import com.google.gson.Gson
import java.io.BufferedReader

class JsonDataManager(private val context: Context) {

    fun loadExercisesFromAssets(): List<Exercise> {
        val jsonString = context.assets.open("exercises.json")
            .bufferedReader().use(BufferedReader::readText)

        val exerciseList = Gson().fromJson(jsonString, ExerciseList::class.java)
        return exerciseList.exercises.sortedBy { it.position }
    }
}