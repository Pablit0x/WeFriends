package com.ps.wefriends.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ps.wefriends.domain.repository.SurveysResponse
import com.ps.wefriends.presentation.components.SurveysList

@Composable
fun HomeContent(surveysResponse: SurveysResponse) {
    SurveysList(
        modifier = Modifier.fillMaxSize(),
        surveysResponse = surveysResponse
    )
}