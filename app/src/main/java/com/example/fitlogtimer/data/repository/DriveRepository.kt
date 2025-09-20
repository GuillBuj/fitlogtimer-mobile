package com.example.fitlogtimer.data.repository

import android.content.Context
import android.util.Log
import com.example.fitlogtimer.data.remote.drive.GoogleDriveManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DriveRepository(private val context: Context) {

    private val driveManager = GoogleDriveManager(context)

    suspend fun uploadWorkoutToDrive(
        fileName: String,
        jsonContent: String
    ): Result<String> = withContext(Dispatchers.IO) {
        Log.d("DriveDebug", "3. Repository - Début upload: $fileName")

        try {
            val result = driveManager.uploadWorkoutFile(fileName, jsonContent)

            if (result.isSuccess) {
                Log.d("DriveDebug", "✅ 3.1 Repository - Upload réussi")
            } else {
                Log.e("DriveDebug", "❌ 3.1 Repository - Upload échoué")
            }

            result

        } catch (e: Exception) {
            Log.e("DriveDebug", "❌ 3.2 Repository - Exception: ${e.message}")
            Result.failure(e)
        }
    }
}