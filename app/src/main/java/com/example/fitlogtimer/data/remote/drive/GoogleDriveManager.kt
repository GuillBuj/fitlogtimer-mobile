package com.example.fitlogtimer.data.remote.drive

import android.content.Context
import com.example.fitlogtimer.data.model.DriveFileInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleDriveManager(private val context: Context) {

    private val driveService = GoogleDriveService(context)

    companion object {
        const val FITLOG_FOLDER_ID = "13zGQYLhRMQAv1eCd_zxY98jsM7NmFviX"
    }

    suspend fun uploadWorkoutFile(fileName: String, jsonContent: String): Result<String> {
        return driveService.uploadFile(fileName, jsonContent, FITLOG_FOLDER_ID)
    }
}