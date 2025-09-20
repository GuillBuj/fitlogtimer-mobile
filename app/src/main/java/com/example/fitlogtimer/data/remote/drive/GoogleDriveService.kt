package com.example.fitlogtimer.data.remote.drive

import android.content.Context
import android.util.Log
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

    companion object {
        const val FITLOG_FOLDER_ID = "1Ukuk_217ZUkXb3A75G2Sedi4Q5t8tMKa"
    }

    suspend fun uploadFile(
        fileName: String,
        content: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d("DriveDebug", "1. Service - Début upload: $fileName")

            val account = GoogleSignIn.getLastSignedInAccount(context)
            if (account == null) {
                Log.e("DriveDebug", "❌ 1.1 Service - Aucun compte Google connecté")
                return@withContext Result.failure(Exception("Non authentifié avec Google"))
            }

            Log.d("DriveDebug", "✅ 1.2 Service - Compte connecté: ${account.email}")

            val fileMetadata = File().apply {
                name = fileName
                mimeType = "application/json"
                parents = listOf(FITLOG_FOLDER_ID)
            }

            val mediaContent = ByteArrayContent("application/json", content.toByteArray())

            Log.d("DriveDebug", "1.3 Service - Appel API Drive...")
            val file = drive.files().create(fileMetadata, mediaContent)
                .setFields("id, name, webViewLink")
                .execute()

            Log.d("DriveDebug", "✅ 1.4 Service - Fichier créé ID: ${file.id}")

            // Rendre le fichier public
            val permission = Permission().apply {
                type = "anyone"
                role = "reader"
            }

            drive.permissions().create(file.id, permission).execute()
            Log.d("DriveDebug", "✅ 1.5 Service - Permissions publiques appliquées")

            Result.success(file.id ?: "")

        } catch (e: Exception) {
            Log.e("DriveDebug", "❌ 1.6 Service - Erreur: ${e.javaClass.simpleName} - ${e.message}")
            Result.failure(e)
        }
    }
}