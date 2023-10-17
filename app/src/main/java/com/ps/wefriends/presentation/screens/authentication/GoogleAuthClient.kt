package com.ps.wefriends.presentation.screens.authentication

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ps.wefriends.util.Constants
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthClient(
    private val signInClient: SignInClient, private val firebaseAuth: FirebaseAuth
) {

    suspend fun googleSignIn(): IntentSender? {
        val result = try {
            signInClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    fun googleSignInWithIntent(
        intent: Intent, onSuccess: () -> Unit, onError: (Exception) -> Unit
    ) {
        val credential = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        firebaseAuth.signInWithCredential(googleCredentials).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                onSuccess()
            } else {
                result.exception?.let { onError(it) }
            }
        }
    }

    fun anonymousSignIn(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        firebaseAuth.signInAnonymously().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                onSuccess()
            } else {
                result.exception?.let { onError(it) }
            }
        }
    }

    suspend fun signOut() {
        try {
            signInClient.signOut().await()
            firebaseAuth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = firebaseAuth.currentUser?.run {
        UserData(
            userId = uid, username = displayName, profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder().setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setFilterByAuthorizedAccounts(false).setServerClientId(Constants.CLIENT_ID).build()
        ).setAutoSelectEnabled(true).build()
    }
}