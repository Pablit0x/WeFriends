package com.ps.wefriends.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ps.wefriends.R
import com.ps.wefriends.presentation.components.PageIndicator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingContent(
    horizontalPagerState: PagerState,
    onSkipButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onboardingItems: List<OnboardingItem>,
) {

    var isPlaying by remember { mutableStateOf(true) }
    val progress by animateLottieCompositionAsState(
        composition = onboardingItems[horizontalPagerState.currentPage].animationSpec?.value, isPlaying = isPlaying
    )

    LaunchedEffect(key1 = progress) {
        if (progress == 0f) {
            isPlaying = true
        }
        if (progress == 1f) {
            isPlaying = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HorizontalPager(state = horizontalPagerState) { currentPageIndex ->
                LottieAnimation(
                    composition = onboardingItems[currentPageIndex].animationSpec?.value, progress = {
                    if (progress == 1f) {
                        isPlaying = true
                    }
                    progress
                })
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = onboardingItems[horizontalPagerState.currentPage].description)

            Spacer(modifier = Modifier.height(12.dp))

            PageIndicator(
                numberOfPages = onboardingItems.size, currentPage = horizontalPagerState.currentPage
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedButton(onClick = onSkipButtonClicked) {
                Text(text = stringResource(id = R.string.skip))
            }

            Button(onClick = onNextButtonClicked) {
                Text(text = stringResource(id = R.string.next))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.NavigateNext,
                    contentDescription = "Next onboarding screen"
                )
            }
        }
    }
}