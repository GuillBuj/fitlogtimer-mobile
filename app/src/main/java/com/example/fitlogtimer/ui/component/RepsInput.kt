package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun RepsInput(
    reps: String,
    onRepsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val current = reps.toIntOrNull() ?: 0
            onRepsChange((current - 1).coerceAtLeast(0).toString())
        }) {
            Icon(Icons.Default.Remove, contentDescription = "Diminuer reps")
        }

        OutlinedTextField(
            value = reps,
            onValueChange = onRepsChange,
            label = { Text("Reps") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = reps.isNotBlank() && reps.toIntOrNull() == null,
            singleLine = true
        )

        IconButton(onClick = {
            val current = reps.toIntOrNull() ?: 0
            onRepsChange((current + 1).toString())
        }) {
            Icon(Icons.Default.Add, contentDescription = "Augmenter reps")
        }
    }
}