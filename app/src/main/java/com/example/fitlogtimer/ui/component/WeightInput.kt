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
import java.util.Locale


@Composable
fun WeightInput(
    weight: String,
    onWeightChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val current = weight.toDoubleOrNull() ?: 0.0
            val newValue = (current - 0.5).coerceAtLeast(0.0)
            onWeightChange("%.1f".format(Locale.US, newValue))
        }) {
            Icon(Icons.Default.Remove, contentDescription = "Diminuer poids")
        }

        OutlinedTextField(
            value = weight,
            onValueChange = onWeightChange,
            label = { Text("Poids (kg)") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = weight.isNotBlank() && weight.toDoubleOrNull() == null,
            singleLine = true
        )

        IconButton(onClick = {
            val current = weight.toDoubleOrNull() ?: 0.0
            val newValue = (current + 0.5).coerceAtLeast(0.0)
            onWeightChange("%.1f".format(Locale.US, newValue))
        }) {
            Icon(Icons.Default.Add, contentDescription = "Augmenter poids")
        }
    }
}