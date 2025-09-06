package com.example.fitlogtimer.data.model

data class ExerciseSet(
    val exerciseId: Int,
    val reps: Int,
    val weight: Double
)


data class WorkoutExport(
    val date: Long = System.currentTimeMillis(),
    val exerciseSets: List<ExerciseSet> = emptyList()
)