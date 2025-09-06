package com.example.fitlogtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.fitlogtimer.data.json.JsonDataManager
import com.example.fitlogtimer.ui.exercise.ExerciseList
import com.example.fitlogtimer.ui.theme.FitLogTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonDataManager = JsonDataManager(this)
        val exercises = jsonDataManager.loadExercisesFromAssets()

        setContent {
            FitLogTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExerciseList(exercises = exercises)
                }
            }
        }
    }
}
