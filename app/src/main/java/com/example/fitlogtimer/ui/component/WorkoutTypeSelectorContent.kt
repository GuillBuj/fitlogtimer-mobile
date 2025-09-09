package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitlogtimer.data.model.WorkoutType
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel

@Composable
fun WorkoutTypeSelectorContent(
    viewModel: ExerciseSetViewModel,
    workoutTypes: List<WorkoutType>,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Option "Aucun"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.updateWorkoutType(null)
                    onDismiss()
                }
                .padding(8.dp)
        ) {
            Text(
                text = "Aucun type spécifique",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Séparateur
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Types d'entraînement
        workoutTypes.forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.updateWorkoutType(type.name)
                        onDismiss()
                    }
                    .padding(8.dp)
            ) {
                Text(
                    text = type.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}