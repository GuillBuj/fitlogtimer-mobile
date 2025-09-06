package com.example.fitlogtimer.ui.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitlogtimer.data.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDropdown(
    exercises: List<Exercise>,
    selectedExerciseId: Int,
    onExerciseSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedExercise = exercises.find { it.id == selectedExerciseId }


    LaunchedEffect(selectedExerciseId) {
        Log.d("ExerciseDropdown", "Selected ID: $selectedExerciseId, name: ${selectedExercise?.name}")
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedExercise?.name ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = { Text("Exercice") },
            placeholder = {
                Text(
                    "Sélectionner un exercice",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            exercises.forEach { exercise ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = exercise.name,
                            fontWeight = if (exercise.id == selectedExerciseId) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    onClick = {
                        Log.d("ExerciseDropdown", "Clicked: ${exercise.name} (id: ${exercise.id})")
                        onExerciseSelected(exercise.id)
                        expanded = false
                    }
                )
            }
        }
    }
}