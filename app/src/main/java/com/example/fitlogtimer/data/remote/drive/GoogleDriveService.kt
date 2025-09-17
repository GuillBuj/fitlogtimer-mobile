package com.example.fitlogtimer.data.remote.drive

import android.content.Context
import com.example.fitlogtimer.data.model.DriveFileInfo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections

class GoogleDriveService(private val context: Context) {

    private val drive: Drive by lazy {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            Collections.singletonList(DriveScopes.DRIVE_FILE)
        )

        val account = GoogleSignIn.getLastSignedInAccount(context)
        account?.let { credential.selectedAccount = it.account }

        Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("FitLogTimer").build()
    }

    suspend fun uploadFile(
        fileName: String,
        content: String,
        folderId: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val fileMetadata = File().apply {
                name = fileName  // CORRIGÉ
                mimeType = "application/json"  // CORRIGÉ
                if (!folderId.isNullOrEmpty()) {
                    parents = listOf(folderId)  // CORRIGÉ
                }
            }

            val mediaContent = ByteArrayContent("application/json", content.toByteArray())

            val file = drive.files().create(fileMetadata, mediaContent)
                .setFields("id, name, createdTime, webViewLink")
                .execute()

            // Rendre le fichier public
            val permission = Permission().apply {
                type = "anyone"  // CORRIGÉ
                role = "reader"  // CORRIGÉ
            }

            drive.permissions().create(file.id, permission).execute()

            Result.success(file.id ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listFilesInFolder(folderId: String): Result<List<DriveFileInfo>> = withContext(Dispatchers.IO) {
        try {
            val result = drive.files().list()
                .setQ("'$folderId' in parents and mimeType='application/json'")
                .setFields("files(id, name, createdTime, webViewLink)")
                .execute()

            val driveFiles = result.files.map { file ->
                DriveFileInfo(
                    id = file.id,
                    name = file.name,
                    createdTime = file.createdTime.toString(),
                    webViewLink = file.webViewLink
                )
            }

            Result.success(driveFiles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}