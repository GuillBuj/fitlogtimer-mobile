package com.example.fitlogtimer.data.model

data class Exercise (
    val id: Int,
    val name: String,
    val shortName: String,
    val position: Int,
    val defaultWeight: Double,
    val defaultReps: Int
)

data class ExerciseList(
    val exercises: List<Exercise>
)