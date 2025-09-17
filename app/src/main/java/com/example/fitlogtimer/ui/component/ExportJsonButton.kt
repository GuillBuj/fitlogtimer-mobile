package com.example.fitlogtimer.ui.component

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel
import com.example.fitlogtimer.ui.viewmodel.ExportStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExportJsonButton(
    viewModel: ExerciseSetViewModel,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    val coroutineScope = rememberCoroutineScope()

    // Observer l'état d'exportation pour afficher les toasts
    LaunchedEffect(viewModel.exportStatus) {
        when (val status = viewModel.exportStatus) {
            is ExportStatus.Success -> {
                Toast.makeText(context, "Exporté vers Drive avec succès", Toast.LENGTH_LONG).show()
                Log.d("Export", "Fichier exporté avec ID: ${status.fileId}")
            }
            is ExportStatus.Error -> {
                Toast.makeText(context, "Erreur d'exportation: ${status.message}", Toast.LENGTH_LONG).show()
                Log.e("Export", "Erreur d'exportation", status.exception)
            }
            ExportStatus.Idle -> {} // Ne rien faire
        }
    }

    Button(
        onClick = {
            coroutineScope.launch {
                try {
                    val timestamp = System.currentTimeMillis()
                    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                    val formatted = sdf.format(Date(timestamp))
                    val fileName = "workout_export_$formatted.json"

                    // Générer le JSON
                    val json = viewModel.exportWorkoutToJson()

                    // Exporter vers Drive via le ViewModel
                    viewModel.exportToDrive(json, fileName)

                    Log.d("Export", "Début exportation: $fileName")

                } catch (e: Exception) {
                    Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("Export", "Erreur lors de la préparation export", e)
                }
            }
        },
        modifier = modifier
    ) {
        Text("Exporter JSON vers Drive")
    }
}