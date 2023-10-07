package com.ps.wefriends.presentation.screens.onboarding

import com.airbnb.lottie.compose.LottieCompositionResult

data class OnboardingItem(
    val index: Int,
    val lottieAnimation: LottieCompositionResult?,
    val description: String
)