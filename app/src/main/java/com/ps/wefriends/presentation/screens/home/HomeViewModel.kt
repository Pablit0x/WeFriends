package com.ps.wefriends.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ps.wefriends.domain.model.GenderAudience
import com.ps.wefriends.domain.model.SurveyType
import com.ps.wefriends.domain.repository.AddSurveyResponse
import com.ps.wefriends.domain.repository.SurveysResponse
import com.ps.wefriends.domain.use_case.SurveyUseCases
import com.ps.wefriends.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth, private val surveyUseCases: SurveyUseCases
) : ViewModel() {


    private val _surveysResponse = MutableStateFlow<SurveysResponse>(Response.Loading)
    val surveysResponse = _surveysResponse.asStateFlow()

    private val _addSurveyResponse = MutableStateFlow<AddSurveyResponse>(Response.Success(false))
    val addSurveyResponse = _addSurveyResponse.asStateFlow()


    private val _isSignOutDialogOpen = MutableStateFlow(false)
    val isSignOutDialogOpen = _isSignOutDialogOpen.asStateFlow()

    init {
        getSurveys()
    }

    fun openSignOutDialog() {
        _isSignOutDialogOpen.update { true }
    }

    fun closeSignOutDialog() {
        _isSignOutDialogOpen.update { false }
    }

    fun signOut() {
        auth.signOut()
    }

    private fun getSurveys() = viewModelScope.launch {
        surveyUseCases.getSurveys().collect { response ->
            delay(5000)
            _surveysResponse.update { response }
        }
    }

    fun addSurvey(
        title: String,
        imageUrl: String? = null,
        surveyType: SurveyType,
        genderAudience: GenderAudience
    ) {
        _addSurveyResponse.update { Response.Loading }
        viewModelScope.launch(Dispatchers.IO) {
            surveyUseCases.addSurvey(
                ownerId = "1234",
                title = title,
                imageUrl = imageUrl,
                surveyType = surveyType.value,
                genderAudience = genderAudience.value
            )
        }
    }

}