package com.ps.wefriends.domain.repository

import com.ps.wefriends.domain.model.Survey
import com.ps.wefriends.util.Response
import kotlinx.coroutines.flow.Flow

typealias Surveys = List<Survey>
typealias SurveysResponse = Response<Surveys>
typealias AddSurveyResponse = Response<Boolean>
interface SurveysRepository {
    fun getSurveys() : Flow<SurveysResponse>

    // FIXME: For testing only...
    suspend fun addSurvey(title: String, imageUrl : String?, surveyType: String, genderAudience: String) : AddSurveyResponse
}