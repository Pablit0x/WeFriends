package com.ps.wefriends.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.wefriends.domain.repository.SurveysRepository
import com.ps.wefriends.presentation.screens.authentication.AuthUiClient
import com.ps.wefriends.domain.model.UserData
import com.ps.wefriends.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val googleAuthClient: AuthUiClient,
    private val repository: SurveysRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSetCurrentUser -> onSetCurrentUser(currentUser = event.userData)
            is HomeEvent.OnFilterChange -> onFilterChange(isOpen = event.isOpen)
            is HomeEvent.OnSearchChange -> onSearchChange(isOpen = event.isOpen)
            is HomeEvent.OnNavigationDrawerChange -> onNavigationDrawerChange(isOpen = event.isOpen)
            is HomeEvent.OnSignOutDialogChange -> onSignOutDialogChange(isOpen = event.isOpen)
            HomeEvent.OnGetSurveys -> onGetSurveys()
            HomeEvent.OnAddPressed -> onNavigateToCreateSurvey()
            HomeEvent.OnFirebaseSignOut -> firebaseSignOut()
            HomeEvent.OnNavigateAuth -> onNavigateAuth()
        }
    }

    private fun onSetCurrentUser(currentUser: UserData?) {
        _state.update {
            it.copy(
                currentUser = currentUser
            )
        }
    }

    private fun onSignOutDialogChange(isOpen: Boolean) {
        _state.update {
            it.copy(
                isSignOutDialogOpen = isOpen
            )
        }
    }

    private fun onSearchChange(isOpen: Boolean) {
        _state.update {
            it.copy(
                isSearchViewOpen = isOpen
            )
        }
    }

    private fun onFilterChange(isOpen: Boolean) {
        _state.update {
            it.copy(
                isFilterViewOpen = isOpen
            )
        }
    }

    private fun firebaseSignOut() {
        viewModelScope.launch(Dispatchers.IO) {
            googleAuthClient.signOut()
        }
    }

    private fun onNavigateToCreateSurvey() {
        viewModelScope.launch {
            _effect.emit(HomeEffect.OnNavigateToCreateSurvey)
        }
    }

    private fun onNavigationDrawerChange(isOpen: Boolean) {
        viewModelScope.launch {
            _effect.emit(HomeEffect.OnNavigationDrawerChange(isOpen = isOpen))
        }
    }

    private fun onNavigateAuth() {
        viewModelScope.launch {
            _effect.emit(HomeEffect.OnNavigateAuth)
        }
    }

    private fun onGetSurveys() = viewModelScope.launch {
        repository.getSurveys().collect { response ->
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