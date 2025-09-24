package com.example.fitlogtimer.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSet(
    @SerializedName("exercise_id")
    val exerciseId: Int,
    val repNumber: Int,
    val weight: Double,
    val bands : String,
    val durationS : Int,
    val distance : String,
    val type: String
)

