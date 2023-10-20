package com.ps.wefriends.presentation.screens.authentication

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.wefriends.domain.model.UserConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    val authClient: AuthUiClient, private val userInfo: DataStore<UserConfig>
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthenticationEffect>()
    var effect = _effect.asSharedFlow()

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.OnAuthenticationChange -> onAuthenticationChange(isAuthenticated = event.isAuthenticated)
            is AuthenticationEvent.OnGoogleLoadingChange -> onGoogleLoadingChange(isLoading = event.isLoading)
            is AuthenticationEvent.OnGuestLoadingChange -> onGuestLoadingChange(isLoading = event.isLoading)
            is AuthenticationEvent.OnSignInAsGuestClicked -> authClient.anonymousSignIn(
                onSuccess = event.onSuccess, onError = event.onError
            )

            is AuthenticationEvent.OnShowSuccessMessage -> showSuccessMessage(event.message)
            is AuthenticationEvent.OnShowErrorMessage -> showErrorMessage(event.exception)
            AuthenticationEvent.OnCheckWhetherOnboardingIsRequired -> onCheckWhetherOnboardingIsRequired()
            AuthenticationEvent.OnNavigateHome -> navigateHome()
            AuthenticationEvent.OnNavigateOnboarding -> navigateOnboarding()
            AuthenticationEvent.OnSignInWithGoogle -> onSignInWithGoogle()
        }
    }

    private fun onAuthenticationChange(isAuthenticated: Boolean) {
        _state.update {
            it.copy(
                isAuthenticated = isAuthenticated
            )
        }
    }

    private fun onGoogleLoadingChange(isLoading: Boolean) {
        _state.update {
            it.copy(
                isGoogleLoading = isLoading
            )
        }
    }


    private fun onGuestLoadingChange(isLoading: Boolean) {
        _state.update {
            it.copy(
                isGuestLoading = isLoading
            )
        }
    }


    private fun onCheckWhetherOnboardingIsRequired() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isOnboardingRequired = isOnboardingRequired()
                )
            }
        }
    }

    private fun onSignInWithGoogle() {
        viewModelScope.launch {
            _effect.emit(AuthenticationEffect.OnSignInWithGoogleClicked)
        }
    }

    private fun showErrorMessage(exception: Exception) {
        viewModelScope.launch {
            _effect.emit(AuthenticationEffect.OnShowErrorMessage(exception = exception))
        }
    }

    private fun showSuccessMessage(successMsg: String) {
        viewModelScope.launch {
            _effect.emit(AuthenticationEffect.OnShowSuccessMessage(message = successMsg))
        }
    }

    private fun navigateHome() {
        viewModelScope.launch {
            _effect.emit(AuthenticationEffect.OnNavigateHome)
        }
    }

    private fun navigateOnboarding() {
        viewModelScope.launch {
            _effect.emit(AuthenticationEffect.OnNavigateOnboarding)
        }
    }

    private suspend fun isOnboardingRequired(): Boolean {
        val deferredResult = viewModelScope.async(Dispatchers.IO) {
            userInfo.data.first().showOnboarding
        }
        return deferredResult.await()
    }
}