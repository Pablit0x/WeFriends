package com.ps.wefriends.domain.repository

import com.ps.wefriends.domain.model.Survey
import com.ps.wefriends.util.Response
import kotlinx.coroutines.flow.Flow


typealias SurveysResponse = Response<Survey>
typealias AddSurveyResponse = Response<Boolean>
interface SurveysRepository {
    fun getSurveys() : Flow<SurveysResponse>

    // FIXME: For testing only...
    fun addSurvey(title: String, imageUri : String?, surveyType: String, genderAudience: String) : AddSurveyResponse
}