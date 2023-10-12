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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val auth: FirebaseAuth, private val surveyUseCases: SurveyUseCases
) : ViewModel() {


    private val _surveysResponse = MutableStateFlow<SurveysResponse>(Response.Loading)
    val surveysResponse = _surveysResponse.asStateFlow()

    private val _addSurveyResponse = MutableStateFlow<AddSurveyResponse>(Response.Success(false))
    val addSurveyResponse = _addSurveyResponse.asStateFlow()

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        getSurveys()
        setUser()
    }

    fun openSignOutDialog() {
        _state.update {
            it.copy(
                isSignOutDialogOpen = true
            )
        }
    }

    fun closeSignOutDialog() {
        _state.update {
            it.copy(
                isSignOutDialogOpen = false
            )
        }
    }

    fun openSearchField() {
        _state.update {
            it.copy(
                isSearchActive = true
            )
        }
    }

    fun closeSearchField() {
        _state.update {
            it.copy(
                isSearchActive = false
            )
        }
    }


    fun openFilterView() {
        _state.update {
            it.copy(
                isFilterActive = true
            )
        }
    }

    fun closeFilterView() {
        _state.update {
            it.copy(
                isFilterActive = false
            )
        }
    }

    fun signOut() {
        auth.signOut()
    }

    private fun setUser() {
        _state.update {
            it.copy(
                currentUser = auth.currentUser
            )
        }
    }

    private fun getSurveys() = viewModelScope.launch {
        surveyUseCases.getSurveys().collect { response ->

            when (response) {
                Response.Loading -> _state.update { it.copy(isLoading = true) }
                is Response.Success -> {
                    _state.update {
                        it.copy(
                            surveys = response.data, isLoading = false
                        )
                    }
                }

                is Response.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false, error = response.e
                        )
                    }
                }
            }
        }
    }

    fun addSurvey(
        ownerId: String,
        title: String,
        imageUrl: String? = null,
        surveyType: SurveyType,
        genderAudience: GenderAudience
    ) {
        _addSurveyResponse.update { Response.Loading }
        viewModelScope.launch(Dispatchers.IO) {
            surveyUseCases.addSurvey(
                ownerId = ownerId,
                title = title,
                imageUrl = imageUrl,
                surveyType = surveyType.value,
                genderAudience = genderAudience.value
            )
        }
    }

}