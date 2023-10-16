package com.ps.wefriends.presentation.screens.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState

@Composable
fun AuthenticationScreen(
    state : AuthenticationState,
    messageBarState: MessageBarState,
    onGuestSignInClicked: () -> Unit,
    onGoogleSignInClicked: () -> Unit,
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
}