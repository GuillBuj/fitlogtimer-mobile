package com.example.fitlogtimer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExport(
    val date: String,
    val bodyWeight: Double? = null,
    val exerciseSets: List<ExerciseSet> = emptyList()
)
