package com.example.fitlogtimer.ui.component

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel
import java.io.File
import java.util.Date
import java.util.Locale

@Composable
fun ExportJsonButton(
    viewModel: ExerciseSetViewModel,
    context: Context,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            val timestamp = System.currentTimeMillis()
            val sdf = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            val formatted = sdf.format(Date(timestamp))
            val fileName = "workout_export_$formatted.json"

            val file = File(context.cacheDir, fileName)
            val json = viewModel.exportWorkoutToJson()
            file.writeText(json)

            val fileUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            // Intent spécifique pour Drive uniquement
            val driveIntent = Intent(Intent.ACTION_SEND).apply {
                `package` = "com.google.android.apps.docs"
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_TITLE, fileName)
            }

            // Vérifier si l'intent peut être résolu
            val resolveInfo = context.packageManager.resolveActivity(
                driveIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            )

            if (resolveInfo != null) {
                context.startActivity(driveIntent)
            } else {
                // si Drive n'est pas installé
                Toast.makeText(context, "Google Drive n'est pas installé", Toast.LENGTH_LONG).show()
            }

            Toast.makeText(context, "Export JSON généré", Toast.LENGTH_SHORT).show()
            Log.d("Export", "Fichier exporté : $fileName")
        },
        modifier = modifier
    ) {
        Text("Exporter JSON vers Drive")
    }
}
