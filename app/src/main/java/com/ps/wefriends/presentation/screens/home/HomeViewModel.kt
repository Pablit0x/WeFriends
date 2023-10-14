package com.ps.wefriends.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ps.wefriends.domain.repository.AddSurveyResponse
import com.ps.wefriends.domain.repository.SurveysResponse
import com.ps.wefriends.domain.use_case.GetSurveys
import com.ps.wefriends.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val auth: FirebaseAuth, private val getSurveys: GetSurveys
) : ViewModel() {


    private val _surveysResponse = MutableStateFlow<SurveysResponse>(Response.Loading)
    val surveysResponse = _surveysResponse.asStateFlow()

    private val _addSurveyResponse = MutableStateFlow<AddSurveyResponse>(Response.Success(false))
    val addSurveyResponse = _addSurveyResponse.asStateFlow()

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSetCurrentUser -> {
                _state.update {
                    it.copy(
                        currentUser = event.currentUser
                    )
                }
            }

            HomeEvent.OnGetSurveys -> {
                getSurveys()
            }

            HomeEvent.OnOpenFilter -> {
                _state.update {
                    it.copy(
                        isFilterViewOpen = true
                    )
                }
            }

            HomeEvent.OnCloseFilter -> {
                _state.update {
                    it.copy(
                        isFilterViewOpen = false
                    )
                }
            }

            HomeEvent.OnOpenSearch -> {
                _state.update {
                    it.copy(
                        isSearchViewOpen = true
                    )
                }
            }

            HomeEvent.OnCloseSearch -> {
                _state.update {
                    it.copy(
                        isSearchViewOpen = false
                    )
                }
            }

            HomeEvent.OnOpenDrawer -> onOpenDrawer()
            HomeEvent.OnCloseDrawer -> onCloseDrawer()
            HomeEvent.OnAddPressed -> onNavigateToCreateSurvey()
            HomeEvent.OnSignOutConfirmed -> signOut()
            HomeEvent.OnCloseSignOutDialog -> {
                _state.update {
                    it.copy(
                        isSignOutDialogOpen = false
                    )
                }
            }

            HomeEvent.OnOpenSignOutDialog -> {
                _state.update {
                    it.copy(
                        isSignOutDialogOpen = true
                    )
                }
            }

            HomeEvent.OnSignOutClicked -> onSignOutButtonClicked()
            HomeEvent.OnSignOutCanceled -> onSignOutCanceled()
        }
    }

    private fun onSignOutButtonClicked() {
        onEvent(HomeEvent.OnOpenSignOutDialog)
        onEvent(HomeEvent.OnCloseDrawer)
    }

    private fun onSignOutCanceled() {
        onEvent(HomeEvent.OnOpenDrawer)
        onEvent(HomeEvent.OnCloseSignOutDialog)
    }

    private fun onNavigateToCreateSurvey() {
        viewModelScope.launch {
            _effect.emit(HomeEffect.OnNavigateToCreateSurvey)
        }
    }

    private fun onOpenDrawer() {
        viewModelScope.launch {
            _effect.emit(HomeEffect.OnOpenDrawer)
        }
    }

    private fun onCloseDrawer() {
        viewModelScope.launch {
            _effect.emit(HomeEffect.OnCloseDrawer)
        }
    }

    private fun onNavigateAuth() {
        viewModelScope.launch {
            _effect.emit(HomeEffect.OnNavigateAuth)
        }
    }

    private fun signOut() {
        onNavigateAuth()
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
        getSurveys.invoke().collect { response ->
            when (response) {
                Response.Loading -> _state.update { it.copy(isDataLoading = true) }
                is Response.Success -> {
                    _state.update {
                        it.copy(
                            surveys = response.data, isDataLoading = false
                        )
                    }
                }

                is Response.Failure -> {
                    _state.update {
                        it.copy(
                            isDataLoading = false, error = response.e
                        )
                    }
                }
            }
        }
    }
}