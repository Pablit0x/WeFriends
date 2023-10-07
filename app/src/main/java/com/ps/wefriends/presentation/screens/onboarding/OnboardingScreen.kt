package com.ps.wefriends.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen() {
    val horizontalPagerState = rememberPagerState(
        pageCount = { 3 }
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OnboardingContent(horizontalPagerState = horizontalPagerState, onSkipButtonClicked = { /*TODO*/ }, onNextButtonClicked = {})
    }
}