package com.ps.wefriends.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ps.wefriends.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navigateHome: () -> Unit) {

    val firstOnboardingItem = OnboardingItem(
        index = 1,
        lottieAnimation = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.morty)),
        description = stringResource(id = R.string.first_onboarding_screen_description)
    )
    val secondOnboardingItem = OnboardingItem(
        index = 2,
        lottieAnimation = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.morty)),
        description = stringResource(id = R.string.second_onboarding_screen_description)
    )
    val thirdOnboardingItem = OnboardingItem(
        index = 3,
        lottieAnimation = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.morty)),
        description = stringResource(id = R.string.third_onboarding_screen_description)
    )

    val onboardingItems = listOf(firstOnboardingItem, secondOnboardingItem, thirdOnboardingItem)
    val horizontalPagerState = rememberPagerState(pageCount = { onboardingItems.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OnboardingContent(horizontalPagerState = horizontalPagerState, onSkipButtonClicked = {
            navigateHome()
        }, onNextButtonClicked = {
            val currentPage = horizontalPagerState.currentPage
            if (currentPage < horizontalPagerState.pageCount) {
                scope.launch {
                    horizontalPagerState.animateScrollToPage(currentPage + 1)
                }
            } else {
                navigateHome()
            }
        }, onboardingItems = onboardingItems
        )
    }
}