package com.ps.wefriends.presentation.screens.create_survey

import com.ps.wefriends.domain.model.GenderAudience
import com.ps.wefriends.domain.model.SurveyType
import com.ps.wefriends.domain.model.UserData

data class CreateSurveyUiState(
    val surveyOwner: UserData? = null,
    val title: String = "",
    val imageUrl : String? = null,
    val genderAudience: GenderAudience = GenderAudience.ANY,
    val surveyType: SurveyType = SurveyType.FORM
)
