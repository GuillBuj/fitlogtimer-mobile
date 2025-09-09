package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BodyWeightIndicator(
    bodyWeight: String,
    modifier: Modifier = Modifier
) {
    if (bodyWeight.isNotEmpty()) {
        Text(
            text = "Poids du corps: ${bodyWeight} kg",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.padding(vertical = 8.dp)
        )
    }
}