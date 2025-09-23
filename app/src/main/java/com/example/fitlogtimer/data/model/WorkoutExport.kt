package com.example.fitlogtimer.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExport(
    val date: String,
    val bodyWeight: Double? = null,
    @SerializedName("name")
    val workoutType: String? = null,
    val sets: List<ExerciseSet> = emptyList()
)
