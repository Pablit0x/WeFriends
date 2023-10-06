package com.ps.wefriends.presentation.screens.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ps.wefriends.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@Composable
fun AuthenticationScreen(
    auth: FirebaseAuth,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    isLoading: Boolean,
    isAuthenticated: Boolean,
    onSignInButtonClicked: () -> Unit,
    onSuccessfulFirebaseSignIn: (String) -> Unit,
    onFailedFirebaseSignIn: (Exception) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateHome: () -> Unit
) {
    LaunchedEffect(key1 = isAuthenticated) {
        if (isAuthenticated) {
            navigateHome()
        }
    }

    Scaffold(
        content = { padding ->
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    isLoading = isLoading,
                    onSignInButtonClicked = { onSignInButtonClicked() },
                    modifier = Modifier.padding(padding)
                )
            }
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    )

    OneTapSignInWithGoogle(state = oneTapSignInState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            val credential = GoogleAuthProvider.getCredential(tokenId, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        onSuccessfulFirebaseSignIn(tokenId)
                    } else {
                        result.exception?.let(onFailedFirebaseSignIn)
                    }
                }
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        })
}