package com.example.fitlogtimer.ui.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fitlogtimer.MainActivity
import com.example.fitlogtimer.service.auth.GoogleAuthService
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel.ExportStatus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExportJsonButton(
    viewModel: ExerciseSetViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authService = remember { GoogleAuthService(context) }

    // Launcher pour le résultat du sign-in
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                // Utilisateur connecté, on peut lancer l'export
                coroutineScope.launch {
                    tryExport(viewModel, authService, context)
                }
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Erreur sign-in: ${e.statusCode}")
            Toast.makeText(context, "Erreur de connexion Google: ${e.statusCode}", Toast.LENGTH_LONG).show()
        }
    }

    // Observer l'état d'exportation
    LaunchedEffect(viewModel.exportStatus) {
        when (val status = viewModel.exportStatus.value) {
            is ExportStatus.Success -> Toast.makeText(context, "✅ Export réussi!", Toast.LENGTH_SHORT).show()
            is ExportStatus.Error -> Toast.makeText(context, "❌ Erreur: ${status.message}", Toast.LENGTH_LONG).show()
            ExportStatus.Idle -> {}
        }
    }

    Button(
        onClick = {
            if (!authService.isUserSignedIn()) {
                // Lancer la connexion Google
                signInLauncher.launch(authService.getSignInIntent())
            } else {
                // Déjà connecté → exporter
                coroutineScope.launch {
                    tryExport(viewModel, authService, context)
                }
            }
        },
        modifier = modifier.width(150.dp)
    ) {
        Text("Exporter vers Drive")
    }
}

// Fonction d'export vers Drive
private suspend fun tryExport(
    viewModel: ExerciseSetViewModel,
    authService: GoogleAuthService,
    context: Context
) {
    if (!authService.isUserSignedIn()) {
        Toast.makeText(context, "Veuillez vous connecter à Google", Toast.LENGTH_LONG).show()
        return
    }

    try {
        val timestamp = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val formatted = sdf.format(Date(timestamp))
        val fileName = "workout_export_$formatted.json"

        val json = viewModel.exportWorkoutToJson()
        viewModel.exportToDrive(json, fileName)

    } catch (e: Exception) {
        Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

