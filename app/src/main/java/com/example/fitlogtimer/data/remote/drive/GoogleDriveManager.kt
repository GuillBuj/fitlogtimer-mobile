package com.example.fitlogtimer.data.remote.drive

import android.content.Context
import android.util.Log
import com.example.fitlogtimer.data.remote.drive.GoogleDriveService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleDriveManager(private val context: Context) {

    private val driveService = GoogleDriveService(context)

    suspend fun uploadWorkoutFile(
        fileName: String,
        jsonContent: String
    ): Result<String> = withContext(Dispatchers.IO) {
        Log.d("DriveDebug", "2. Manager - Début traitement: $fileName")

        val result = driveService.uploadFile(fileName, jsonContent)

        when {
            result.isSuccess -> {
                Log.d("DriveDebug", "✅ 2.1 Manager - Succès: ${result.getOrNull()}")
            }
            else -> {
                Log.e("DriveDebug", "❌ 2.1 Manager - Échec: ${result.exceptionOrNull()?.message}")
            }
        }

        result
    }
}