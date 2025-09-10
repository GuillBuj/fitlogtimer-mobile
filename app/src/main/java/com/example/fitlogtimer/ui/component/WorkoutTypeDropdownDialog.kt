package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTypeDropdownDialog(viewModel: ExerciseSetViewModel) {
    val workoutTypes = viewModel.workoutTypes
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(viewModel.selectedWorkoutType) }

    AlertDialog(
        onDismissRequest = { viewModel.toggleWorkoutTypeSelector() },
        title = { Text("Type d'entraînement") },
        text = {
            Column {
                // Dropdown pour sélectionner le type
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedType ?: "",
                        onValueChange = {},
                        label = { Text("Choisir un type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        // Option "Aucun"
                        DropdownMenuItem(
                            text = { Text("Aucun type spécifique") },
                            onClick = {
                                selectedType = null
                                expanded = false
                            }
                        )

                        Divider()

                        // Types d'entraînement
                        workoutTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    selectedType = type.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.updateWorkoutType(selectedType)
                    viewModel.toggleWorkoutTypeSelector()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.toggleWorkoutTypeSelector() }
            ) {
                Text("Annuler")
            }
        }
    )
}