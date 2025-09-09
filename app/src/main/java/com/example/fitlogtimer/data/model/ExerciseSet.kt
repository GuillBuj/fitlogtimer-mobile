package com.example.fitlogtimer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSet(
    val exerciseId: Int,
    val reps: Int,
    val weight: Double
)

