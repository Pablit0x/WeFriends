package com.ps.wefriends.presentation.screens.authentication

sealed class AuthenticationEvent {
    data class OnGoogleLoadingChange(val isLoading: Boolean) : AuthenticationEvent()
    data class OnGuestLoadingChange(val isLoading: Boolean) : AuthenticationEvent()
    data class OnAuthenticationChange(val isAuthenticated: Boolean) : AuthenticationEvent()
    data object OnCheckWhetherOnboardingIsRequired : AuthenticationEvent()

    data class OnSignInAsGuestClicked(val onSuccess: () -> Unit, val onError: (Exception) -> Unit) :
        AuthenticationEvent()

    data object OnSignInWithGoogle : AuthenticationEvent()

    data object OnNavigateHome : AuthenticationEvent()

    data object OnNavigateOnboarding : AuthenticationEvent()

    data class OnShowSuccessMessage(val message: String) : AuthenticationEvent()
    data class OnShowErrorMessage(val exception: Exception) : AuthenticationEvent()
}