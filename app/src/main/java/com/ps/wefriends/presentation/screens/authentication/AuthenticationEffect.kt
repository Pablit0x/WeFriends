package com.ps.wefriends.presentation.screens.authentication

sealed class AuthenticationEffect {

    data object OnNavigateHome : AuthenticationEffect()

    data object OnNavigateOnboarding : AuthenticationEffect()

    data object OnSignInWithGoogleClicked : AuthenticationEffect()

    data class OnShowSuccessMessage(val message: String) : AuthenticationEffect()

    data class OnShowErrorMessage(val exception: Exception) : AuthenticationEffect()
}