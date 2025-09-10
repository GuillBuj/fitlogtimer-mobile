package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel

@Composable
fun BodyWeightInputIcon(
    viewModel: ExerciseSetViewModel,
    modifier: Modifier = Modifier
) {
    val bodyWeight = viewModel.bodyWeight
    val showDialog = viewModel.showBodyWeightDialog

    // Icône avec indicateur si poids saisi
    Box(modifier = modifier) {
        IconButton(
            onClick = { viewModel.toggleBodyWeightDialog() }
        ) {
            Icon(
                imageVector = Icons.Default.Scale,
                contentDescription = "Poids du corps",
                tint = if (bodyWeight.isNotEmpty()) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }

        // Badge indicateur
        if (bodyWeight.isNotEmpty()) {
            Badge(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("✓", style = MaterialTheme.typography.labelSmall)
            }
        }
    }

    // Dialogue pour saisir le poids
    if (showDialog) {
        BodyWeightInputDialog(viewModel = viewModel)
    }
}