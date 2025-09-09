package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel

@Composable
fun WorkoutTypeSelectorButton(
    viewModel: ExerciseSetViewModel,
    modifier: Modifier = Modifier
) {
    val selectedWorkoutType = viewModel.selectedWorkoutType
    val workoutTypes = viewModel.workoutTypes

    // État INTERNE pour l'affichage
    var showSelector by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Bouton pour ouvrir/fermer le sélecteur
        OutlinedButton(
            onClick = { showSelector = !showSelector },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = "Type d'entraînement"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(selectedWorkoutType ?: "Choisir le type")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = if (showSelector) Icons.Default.ArrowDropUp
                else Icons.Default.ArrowDropDown,
                contentDescription = if (showSelector) "Fermer" else "Ouvrir"
            )
        }

        // Selector qui s'affiche seulement si showSelector = true
        if (showSelector && workoutTypes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            WorkoutTypeSelectorContent(
                viewModel = viewModel,
                workoutTypes = workoutTypes,
                onDismiss = { showSelector = false }  // Fermer localement
            )
        }

        // Affichage du type sélectionné (quand fermé)
        if (selectedWorkoutType != null && !showSelector) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Type: $selectedWorkoutType",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}