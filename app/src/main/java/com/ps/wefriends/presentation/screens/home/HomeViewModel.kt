package com.ps.wefriends.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSetCurrentUser -> onSetCurrentUser(currentUser = event.currentUser)
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

    private fun onSetCurrentUser(currentUser : FirebaseUser?){
        _state.update {
            it.copy(
                currentUser = currentUser
            )
        }
    }

    private fun onSignOutDialogChange(isOpen: Boolean){
        _state.update {
            it.copy(
                isSignOutDialogOpen = isOpen
            )
        }
    }

    private fun onSearchChange(isOpen: Boolean){
        _state.update {
            it.copy(
                isSearchViewOpen = isOpen
            )
        }
    }

    private fun onFilterChange(isOpen: Boolean){
        _state.update {
            it.copy(
                isFilterViewOpen = isOpen
            )
        }
    }

    private fun firebaseSignOut(){
        auth.signOut()
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