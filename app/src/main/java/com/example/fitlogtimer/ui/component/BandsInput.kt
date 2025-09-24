package com.example.fitlogtimer.ui.component

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BandsInput(
    bands: String,
    onBandsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = bands,
        onValueChange = onBandsChange,
        label = { Text("Bandes") },
        modifier = modifier,
        singleLine = true
    )
}
