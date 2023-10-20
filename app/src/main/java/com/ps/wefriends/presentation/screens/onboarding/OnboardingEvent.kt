package com.ps.wefriends.presentation.screens.onboarding

sealed class OnboardingEvent {
    data object OnCompleteOnboarding: OnboardingEvent()
    data object OnNavigateHome: OnboardingEvent()

    data object OnNavigateAuth : OnboardingEvent()
}