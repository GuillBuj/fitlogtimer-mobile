package com.example.fitlogtimer.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun Timer() {
    var timeLeft by remember { mutableIntStateOf(60) } // en secondes
    var isRunning by remember { mutableStateOf(false) }

    // Timer countdown
    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        if (timeLeft == 0) {
            isRunning = false
        }
    }

    // Format mm:ss
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Affichage du temps + boutons Start/Pause/Reset
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Temps : $formattedTime",
                style = MaterialTheme.typography.headlineSmall
            )

            IconButton(onClick = {
                isRunning = !isRunning
            }) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Démarrer"
                )
            }

            IconButton(onClick = {
                timeLeft = 60
                isRunning = false
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Réinitialiser")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Boutons +/- 15s
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                timeLeft = maxOf(0, timeLeft - 15)
                isRunning = false
            }) {
                Text("-15 sec")
            }

            Button(onClick = {
                timeLeft += 15
                isRunning = false
            }) {
                Text("+15 sec")
            }
        }
    }
}

