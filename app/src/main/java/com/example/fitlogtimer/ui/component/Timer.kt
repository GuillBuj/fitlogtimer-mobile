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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.HourglassFull
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import kotlin.math.abs

@Composable
fun Timer() {
    var timeLeft by remember { mutableIntStateOf(60) }
    var isRunning by remember { mutableStateOf(false) }
    var targetTime by remember { mutableIntStateOf(60) } // ← NOUVEAU: valeur cible

    // Définition des couleurs foncées
    val darkGreen = Color(0xFF2E7D32)
    val lightGreen = Color(0xFFC5E1A5)
    val darkRed = Color(0xFFC62828)
    val lightRed = Color(0xFFEF5350)
    val darkGray = Color(0xFF616161)
    val lightGray = Color(0xFFE0E0E0)

    // Timer countdown - utilise targetTime comme référence
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            timeLeft--
        }
    }

    // Format mm:ss
    val minutes = abs(timeLeft) / 60
    val seconds = abs(timeLeft) % 60
    val formattedTime = String.format("%s%02d:%02d",
        if (timeLeft < 0) "-" else "", minutes, seconds)

    // Couleur dynamique
    val textColor = if (timeLeft < 0) darkRed else MaterialTheme.colorScheme.onBackground
    val iconColor = if (timeLeft < 0) lightRed else if (isRunning) lightGreen else lightGray

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = {
                    timeLeft = targetTime // ← Reset avec la valeur cible actuelle
                    isRunning = true
                },
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = darkGray,
                    contentColor = lightGray
                )
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.HourglassTop else Icons.Default.HourglassEmpty,
                    contentDescription = "Lancer le timer",
                    modifier = Modifier.size(32.dp),
                    tint = iconColor
                )
            }

            Text(
                text = formattedTime,
                style = MaterialTheme.typography.headlineSmall,
                color = textColor
            )

            IconButton(
                onClick = {
                    targetTime = maxOf(15, targetTime - 15) // Min 15 secondes
                    timeLeft = targetTime
                    isRunning = false
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = lightGray,
                    contentColor = darkGray
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "-15 sec"
                )
            }

            IconButton(
                onClick = {
                    targetTime += 15
                    timeLeft = targetTime
                    isRunning = false
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = lightGray,
                    contentColor = darkGray
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "+15 sec"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Affichage de la durée cible
        Text(
            text = "Durée: ${targetTime}s",
            style = MaterialTheme.typography.bodySmall,
            color = darkGray
        )
    }
}