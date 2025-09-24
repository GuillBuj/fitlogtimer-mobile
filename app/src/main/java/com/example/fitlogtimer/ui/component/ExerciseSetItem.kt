package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitlogtimer.data.model.Exercise
import com.example.fitlogtimer.data.model.ExerciseSet

@Composable
fun ExerciseSetItem(
    exerciseSet: ExerciseSet,
    exercise: Exercise?,
    index: Int,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "#${index + 1} - ${exercise?.name ?: "Exercice ${exerciseSet.exerciseId}"}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            when (exerciseSet.type) {
                "FREE_WEIGHT" -> {
                    Text("${exerciseSet.repNumber} × ${exerciseSet.weight} kg")
                }
                "ELASTIC" -> {
                    val parts = listOfNotNull(
                        "${exerciseSet.repNumber} × @${exerciseSet.bands}",
                        if (exerciseSet.weight > 0) "+${exerciseSet.weight}kg" else null
                    )
                    Text(parts.joinToString(" "))
                }
                "ISOMETRIC" -> {
                    val parts = listOfNotNull(
                        "${exerciseSet.durationS}s",
                        if (exerciseSet.weight > 0) "+${exerciseSet.weight}kg" else null
                    )
                    Text(parts.joinToString(" "))
                }
                "BODYWEIGHT" -> {
                    val parts = listOfNotNull(
                        "${exerciseSet.repNumber} ×",
                        if (exerciseSet.weight > 0) "+${exerciseSet.weight}kg" else null,
                        if (exerciseSet.bands.isNotBlank()) "@${exerciseSet.bands}" else null
                    )
                    Text(parts.joinToString(" "))
                }
                "MOVEMENT" -> {
                    val parts = listOfNotNull(
                        if (exerciseSet.repNumber > 0) "${exerciseSet.repNumber} ×" else null,
                        exerciseSet.distance.takeIf { it.isNotBlank() },
                        if (exerciseSet.weight > 0) "${exerciseSet.weight}kg" else null,
                        if (exerciseSet.bands.isNotBlank()) "@${exerciseSet.bands}" else null
                    )
                    Text(parts.joinToString(" ").trim())
                }

                else -> {
                    // Fallback pour les types non reconnus
                    if (exerciseSet.durationS > 0) {
                        Text("Durée : ${exerciseSet.durationS}s")
                    }
                    if (exerciseSet.repNumber > 0) {
                        Text("${exerciseSet.repNumber} répétitions")
                    }
                    if (exerciseSet.weight > 0) {
                        Text("Poids : ${exerciseSet.weight} kg")
                    }
                    if (exerciseSet.bands.isNotBlank()) {
                        Text("Bandes : ${exerciseSet.bands}")
                    }
                    if (exerciseSet.distance.isNotBlank()) {
                        Text("Distance : ${exerciseSet.distance}")
                    }
                }
            }
        }
    }
}