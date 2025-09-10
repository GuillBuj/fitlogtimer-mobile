package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel

@Composable
fun BodyWeightInputDialog(viewModel: ExerciseSetViewModel) {
    var tempWeight by remember { mutableStateOf(viewModel.bodyWeight) }

    AlertDialog(
        onDismissRequest = { viewModel.toggleBodyWeightDialog() },
        title = { Text("Votre poids du corps") },
        text = {
            Column {
                OutlinedTextField(
                    value = tempWeight,
                    onValueChange = { tempWeight = it },
                    label = { Text("Poids (kg)") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Facultatif - pour ratios éventuellement",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.updateBodyWeight(tempWeight)
                    viewModel.toggleBodyWeightDialog()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.toggleBodyWeightDialog() }
            ) {
                Text("Annuler")
            }
        }
    )
}