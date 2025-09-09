package com.example.fitlogtimer.data.model

data class WorkoutType(
    val name: String
) {
}

data class WorkoutTypeList(
    val workoutTypes: List<WorkoutType>
){
}