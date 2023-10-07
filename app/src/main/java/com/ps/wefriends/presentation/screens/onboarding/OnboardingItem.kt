package com.ps.wefriends.presentation.screens.onboarding

import com.airbnb.lottie.compose.LottieCompositionResult

data class OnboardingItem(
    val index: Int,
    val animationSpec: LottieCompositionResult?,
    val description: String
)