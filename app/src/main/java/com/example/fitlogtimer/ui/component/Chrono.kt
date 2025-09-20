package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.Color

@Composable
fun Chrono() {
    var timeInSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            timeInSeconds++
        }
    }

    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // ← Centrage principal
    ) {
        // Les éléments avec espacement entre eux
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp) // ← Espacement interne
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Sablier",
                modifier = Modifier.size(32.dp),
                tint = Color.DarkGray
            )

            Text(
                text = formattedTime,
                style = MaterialTheme.typography.headlineSmall
            )

            IconButton(onClick = { isRunning = !isRunning }) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Démarrer"
                )
            }

            IconButton(onClick = {
                timeInSeconds = 0
                isRunning = false
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Réinitialiser"
                )
            }
        }
    }
}
