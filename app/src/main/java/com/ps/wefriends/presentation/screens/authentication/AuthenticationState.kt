package com.ps.wefriends.presentation.screens.authentication

data class AuthenticationState(
    val isGoogleLoading: Boolean = false,
    val isGuestLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isOnboardingRequired : Boolean = true,

)
