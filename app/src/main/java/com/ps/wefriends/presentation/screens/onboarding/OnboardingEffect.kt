package com.ps.wefriends.presentation.screens.onboarding

sealed class OnboardingEffect {
    data object OnNavigateHome: OnboardingEffect()
    data object OnNavigateAuth: OnboardingEffect()
}