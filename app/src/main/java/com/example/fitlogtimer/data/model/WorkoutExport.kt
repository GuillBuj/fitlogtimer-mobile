package com.example.fitlogtimer.data.model

import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class WorkoutExport(
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val bodyWeight: Double? = null,
    val exerciseSets: List<ExerciseSet> = emptyList()
)
