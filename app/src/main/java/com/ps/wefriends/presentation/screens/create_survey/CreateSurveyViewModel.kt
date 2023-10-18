package com.ps.wefriends.presentation.screens.create_survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.wefriends.domain.repository.SurveysRepository
import com.ps.wefriends.presentation.screens.authentication.AuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSurveyViewModel @Inject constructor(
    private val authUiClient: AuthUiClient, private val repository: SurveysRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateSurveyState())
    val state = _state.asStateFlow()

    fun setUserOwner() {
        _state.update {
            it.copy(
                surveyOwner = authUiClient.getSignedInUser()
            )
        }
    }

    fun onTitleTextChanged(text: String) {
        _state.update {
            it.copy(
                title = text
            )
        }
    }

    fun createSurvey() {
        state.value.run {
            viewModelScope.launch(Dispatchers.IO) {
                repository.createSurvey(
                    ownerId = surveyOwner!!.userId,
                    title = title,
                    imageUrl = imageUrl,
                    surveyType = surveyType.value,
                    genderAudience = genderAudience.value
                )
            }
        }
    }
}