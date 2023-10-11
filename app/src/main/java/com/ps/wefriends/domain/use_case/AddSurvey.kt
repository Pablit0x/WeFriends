package com.ps.wefriends.domain.use_case

import com.ps.wefriends.domain.repository.SurveysRepository

class AddSurvey(
    private val repo: SurveysRepository) {
    operator fun invoke(
        title: String,
        imageUri: String? = null,
        surveyType: String,
        genderAudience: String
    ) = repo.addSurvey(title = title, imageUri = imageUri, surveyType = surveyType, genderAudience = genderAudience)
}