package com.example.fitlogtimer.ui.component
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.fitlogtimer.service.auth.GoogleAuthService
import com.example.fitlogtimer.ui.viewmodel.ExerciseSetViewModel

@Composable
fun ExportJsonButton(
    viewModel: ExerciseSetViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authService = GoogleAuthService(context)

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(ApiException::class.java)
            if (account != null) {
                coroutineScope.launch {
                    tryExport(viewModel, authService, context)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Erreur de connexion Google", Toast.LENGTH_LONG).show()
        }
    }

    Button(
        onClick = {
            if (!authService.isUserSignedIn()) {
                signInLauncher.launch(authService.getSignInIntent())
            } else {
                coroutineScope.launch {
                    tryExport(viewModel, authService, context)
                }
            }
        },
        modifier = modifier
    ) {
        Text("Exporter vers Drive")
    }
}

private suspend fun tryExport(
    viewModel: ExerciseSetViewModel,
    authService: GoogleAuthService,
    context: Context
) {
    try {
        val json = viewModel.exportWorkoutToJson()
        val timestamp = System.currentTimeMillis()
        val fileName = "workout_export_$timestamp.json"

        viewModel.exportToDrive(json, fileName)
        Toast.makeText(context, "✅ Export réussi!", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        Toast.makeText(context, "❌ Erreur: ${e.message}", Toast.LENGTH_LONG).show()
    }
}