package com.ps.wefriends.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ps.wefriends.presentation.components.SurveysList

@Composable
fun HomeContent(state: HomeUiState) {
    SurveysList(
        modifier = Modifier.fillMaxSize(),
        state = state
    )
}