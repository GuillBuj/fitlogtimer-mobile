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
fun DurationInput(
    duration: String,
    onDurationChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val current = duration.toIntOrNull() ?: 0
            onDurationChange((current - 1).coerceAtLeast(0).toString())
        }) {
            Icon(Icons.Default.Remove, contentDescription = "Diminuer durée")
        }

        OutlinedTextField(
            value = duration,
            onValueChange = onDurationChange,
            label = { Text("Durée (s)") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = duration.isNotBlank() && duration.toIntOrNull() == null,
            singleLine = true
        )

        IconButton(onClick = {
            val current = duration.toIntOrNull() ?: 0
            onDurationChange((current + 1).toString())
        }) {
            Icon(Icons.Default.Add, contentDescription = "Augmenter durée")
        }
    }
}