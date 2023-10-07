package com.ps.wefriends.presentation.screens.onboarding

import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.ps.wefriends.R

data class OnboardingItem(
    val index: Int,
    val animationSpec: LottieCompositionResult?,
    val description: String
)