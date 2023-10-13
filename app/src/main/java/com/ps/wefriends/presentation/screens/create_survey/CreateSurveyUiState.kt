package com.ps.wefriends.presentation.screens.create_survey

import com.ps.wefriends.domain.model.GenderAudience
import com.ps.wefriends.domain.model.SurveyType

data class CreateSurveyUiState(
    val title: String = "",
    val imageUrl : String? = null,
    val genderAudience: GenderAudience = GenderAudience.ANY,
    val surveyType: SurveyType = SurveyType.FORM
)
