package com.ps.wefriends.presentation.screens.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
    state : AuthenticationUiState,
    firebaseAuth: FirebaseAuth,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    onGuestSignInClicked: () -> Unit,
    onGoogleSignInClicked: () -> Unit,
    onSuccessfulFirebaseSignIn: (String) -> Unit,
    onFailedFirebaseSignIn: (Exception) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateHome: () -> Unit,
    navigateOnboarding: () -> Unit
) {

    LaunchedEffect(key1 = state.isAuthenticated, key2 = state.isOnboardingRequired) {
            if (state.isAuthenticated && state.isOnboardingRequired) {
                navigateOnboarding()
            } else if (state.isAuthenticated) {
                navigateHome()
            }
    }

    Scaffold(
        content = { padding ->
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    isGuestLoading = state.isGuestLoading,
                    isGoogleLoading = state.isGoogleLoading,
                    onGuestSignInClicked = onGuestSignInClicked,
                    onGoogleSignInClicked = onGoogleSignInClicked,
                    modifier = Modifier.padding(padding)
                )
            }
        }, modifier = Modifier
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
    )

    OneTapSignInWithGoogle(state = oneTapSignInState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            val credential = GoogleAuthProvider.getCredential(tokenId, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { result ->
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