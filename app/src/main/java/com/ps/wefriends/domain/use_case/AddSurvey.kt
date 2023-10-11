package com.ps.wefriends.domain.use_case

import com.ps.wefriends.domain.repository.SurveysRepository

class AddSurvey(
    private val repo: SurveysRepository) {
    suspend operator fun invoke(
        title: String,
        imageUrl: String? = null,
        surveyType: String,
        genderAudience: String
    ) = repo.addSurvey(title = title, imageUrl = imageUrl, surveyType = surveyType, genderAudience = genderAudience)
}