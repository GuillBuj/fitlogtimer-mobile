package com.example.fitlogtimer.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitlogtimer.data.manager.JsonDataManager
import com.example.fitlogtimer.data.repository.DataRepository
import com.example.fitlogtimer.ui.component.BodyWeightInput
import com.example.fitlogtimer.ui.component.ExerciseDropdown
import com.example.fitlogtimer.ui.component.ExportJsonButton
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModelFactory
import java.io.File
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun ExerciseSetFormScreen() {
    val context = LocalContext.current
    val jsonDataManager = remember { JsonDataManager(context) }
    val repository = remember { DataRepository(jsonDataManager) }

    val viewModel: ExerciseSetViewModel = viewModel(
        factory = ExerciseSetViewModelFactory(repository, jsonDataManager)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.selectedExerciseId) {
        Log.d("ExerciseSetFormScreen", "UI state changed: selectedExerciseId = ${uiState.selectedExerciseId}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Ajouter des séries",
                style = MaterialTheme.typography.headlineMedium
            )

            BodyWeightInput(viewModel = viewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Chargement des exercices...")
            }
        } else if (uiState.exercises.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Aucun exercice trouvé dans le fichier JSON")
            }
        } else {

            ExerciseDropdown(
                exercises = uiState.exercises,
                selectedExerciseId = uiState.selectedExerciseId,
                onExerciseSelected = viewModel::updateSelectedExercise
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- Reps ---
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        val current = uiState.reps.toIntOrNull() ?: 0
                        viewModel.updateReps((current - 1).coerceAtLeast(0).toString())
                    }) {
                        Icon(Icons.Default.Remove, contentDescription = "Diminuer reps")

                    }

                    OutlinedTextField(
                        value = uiState.reps,
                        onValueChange = viewModel::updateReps,
                        label = { Text("Reps") },
                        modifier = Modifier.weight(1f),
                        isError = uiState.reps.isNotBlank() && uiState.reps.toIntOrNull() == null,
                        singleLine = true
                    )

                    IconButton(onClick = {
                        val current = uiState.reps.toIntOrNull() ?: 0
                        viewModel.updateReps((current + 1).toString())
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Augmenter reps")
                    }
                }

                // --- Poids ---
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        val current = uiState.weight.toDoubleOrNull() ?: 0.0
                        val newValue = (current - 0.5).coerceAtLeast(0.0)
                        viewModel.updateWeight("%.1f".format(Locale.US, newValue))//Locale.US pour eviter erreur
                    }) {
                        Icon(Icons.Default.Remove, contentDescription = "Diminuer poids")
                    }

                    OutlinedTextField(
                        value = uiState.weight,
                        onValueChange = viewModel::updateWeight,
                        label = { Text("Poids (kg)") },
                        modifier = Modifier.weight(1f),
                        isError = uiState.weight.isNotBlank() && uiState.weight.toDoubleOrNull() == null,
                        singleLine = true
                    )

                    IconButton(onClick = {
                        val current = uiState.weight.toDoubleOrNull() ?: 0.0
                        val newValue = (current + 0.5).coerceAtLeast(0.0)
                        viewModel.updateWeight("%.1f".format(Locale.US, newValue))
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Augmenter poids")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::addExerciseSet,
                enabled = uiState.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ajouter l'exercice")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (uiState.exerciseSets.isNotEmpty()) {
                Text(
                    "Exercices ajoutés (${uiState.exerciseSets.size})",
                    style = MaterialTheme.typography.headlineSmall
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )

                uiState.exerciseSets.forEachIndexed { index, exerciseSet ->
                    val exercise = uiState.exercises.find { it.id == exerciseSet.exerciseId }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "#${index + 1} - ${exercise?.name ?: "Exercice ${exerciseSet.exerciseId}"}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                            Text("${exerciseSet.reps} reps × ${exerciseSet.weight} kg")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = viewModel::clearWorkout,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Effacer tout")
                    }

                    ExportJsonButton(
                        viewModel = viewModel,
                        context = LocalContext.current,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Text(
                    "Aucun exercice ajouté. Sélectionnez un exercice pour commencer !",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}