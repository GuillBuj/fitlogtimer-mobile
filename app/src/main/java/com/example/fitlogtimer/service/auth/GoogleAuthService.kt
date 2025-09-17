package com.example.fitlogtimer.service.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Tasks
import com.google.api.services.drive.DriveScopes
import java.util.concurrent.TimeUnit

class GoogleAuthService(private val context: Context) {

    private val googleSignInClient by lazy {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, signInOptions)
    }

    suspend fun signIn(activity: Activity): Boolean {
        return try {
            val signInIntent = googleSignInClient.signInIntent
            val task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
            val result = Tasks.await(task, 30, TimeUnit.SECONDS)
            result != null
        } catch (e: Exception) {
            false
        }
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    suspend fun signOut() {
        val task = googleSignInClient.signOut()
        Tasks.await(task, 30, TimeUnit.SECONDS)
    }

    fun isUserSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context) != null
    }

    fun getAccountEmail(): String? {
        return GoogleSignIn.getLastSignedInAccount(context)?.email
    }
}