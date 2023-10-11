package com.ps.wefriends.data.repository

import com.ps.wefriends.domain.repository.AddSurveyResponse
import com.ps.wefriends.domain.repository.SurveysRepository
import com.ps.wefriends.domain.repository.SurveysResponse
import kotlinx.coroutines.flow.Flow

class SurveysRepositoryImpl(
    val surveysCollectionRef : CollectionReference
) : SurveysRepository {
    override fun getSurveys(): Flow<SurveysResponse> {
        TODO("Not yet implemented")
    }

    override fun addSurvey(
        title: String,
        imageUri: String?,
        surveyType: String,
        genderAudience: String
    ): AddSurveyResponse {
        TODO("Not yet implemented")
    }
}