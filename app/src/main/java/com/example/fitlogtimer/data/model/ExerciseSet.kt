package com.example.fitlogtimer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSet(
    val exerciseId: Int,
    val reps: Int,
    val weight: Double
)

@Serializable
data class WorkoutExport(
    val date: Long = System.currentTimeMillis(),
    val exerciseSets: List<ExerciseSet> = emptyList()
)