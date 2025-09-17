package com.example.fitlogtimer.data.repository

import android.content.Context
import com.example.fitlogtimer.data.model.DriveFileInfo
import com.example.fitlogtimer.data.remote.drive.GoogleDriveManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DriveRepository(private val context: Context) {

    private val driveManager = GoogleDriveManager(context)

    suspend fun uploadWorkoutToDrive(
        fileName: String,
        jsonContent: String
    ): Result<String> {
        return driveManager.uploadWorkoutFile(fileName, jsonContent)
    }
}