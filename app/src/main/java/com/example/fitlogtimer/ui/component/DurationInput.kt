package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DurationInput(
    duration: String,
    onDurationChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = duration,
        onValueChange = onDurationChange,
        label = { Text("Durée (s)") },
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        isError = duration.isNotBlank() && duration.toIntOrNull() == null,
        singleLine = true
    )
}
