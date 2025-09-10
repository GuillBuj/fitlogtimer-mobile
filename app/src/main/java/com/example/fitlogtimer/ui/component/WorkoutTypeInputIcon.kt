package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel

@Composable
fun WorkoutTypeInputIcon(
    viewModel: ExerciseSetViewModel,
    modifier: Modifier = Modifier
) {
    val selectedWorkoutType = viewModel.selectedWorkoutType
    val showSelector = viewModel.showWorkoutTypeSelector

    // Icône avec indicateur si type sélectionné
    Box(modifier = modifier) {
        IconButton(
            onClick = { viewModel.toggleWorkoutTypeSelector() }
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = "Type d'entraînement",
                tint = if (selectedWorkoutType != null) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }

        // Badge indicateur
        if (selectedWorkoutType != null) {
            Badge(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("✓", style = MaterialTheme.typography.labelSmall)
            }
        }
    }

    // Dialogue dropdown
    if (showSelector) {
        WorkoutTypeDropdownDialog(viewModel = viewModel)
    }

    // Affichage du type sélectionné en dessous de l'icône
    if (selectedWorkoutType != null) {
        Text(
            text = "Type: $selectedWorkoutType",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}