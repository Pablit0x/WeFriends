package com.ps.wefriends.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ps.wefriends.R
import com.ps.wefriends.presentation.components.PageIndicator

data class OnboardingAnimationItem(
    val index: Int, val animation: LottieComposition?
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingContent(
    horizontalPagerState: PagerState,
    onSkipButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit
) {
    val firstAnimation = OnboardingAnimationItem(
        index = 1,
        animation = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.google.firebase.R.raw.firebase_common_keep)).value
    )

    val secondAnimation = OnboardingAnimationItem(
        index = 2,
        animation = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.google.firebase.R.raw.firebase_common_keep)).value
    )

    val thirdAnimation = OnboardingAnimationItem(
        index = 3,
        animation = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.google.firebase.R.raw.firebase_common_keep)).value
    )

    val animations = listOf(firstAnimation, secondAnimation, thirdAnimation)

    var isPlaying by remember { mutableStateOf(true) }
    val progress by animateLottieCompositionAsState(
        composition = animations[horizontalPagerState.currentPage].animation, isPlaying = isPlaying
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(state = horizontalPagerState) { currentPageIndex ->
            LottieAnimation(composition = animations[currentPageIndex].animation, progress = {
                if (progress == 1f) {
                    isPlaying = true
                }
                progress
            })
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")

        Spacer(modifier = Modifier.height(12.dp))

        PageIndicator(numberOfPages = animations.size, currentPage = horizontalPagerState.currentPage)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedButton(onClick = onSkipButtonClicked) {
                Text(text = stringResource(id = R.string.skip))
            }

            Button(onClick = onNextButtonClicked) {
                Text(text = stringResource(id = R.string.next))
            }
        }
    }
}