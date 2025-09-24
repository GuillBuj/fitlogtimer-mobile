package com.example.fitlogtimer.data.model

data class Exercise (
    val id: Int,
    val name: String,
    val shortName: String,
    val position: Int,
    val defaultWeight: Double,
    val defaultReps: Int,
    val defaultBands : String,
    val defaultDurationS : Int,
    val defaultDistance : String,
    val type: String
)

data class ExerciseList(
    val exercises: List<Exercise>
)