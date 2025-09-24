package com.example.fitlogtimer.ui.component

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DistanceInput(
    distance: String,
    onDistanceChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = distance,
        onValueChange = onDistanceChange,
        label = { Text("Distance") },
        modifier = modifier,
        singleLine = true
    )
}