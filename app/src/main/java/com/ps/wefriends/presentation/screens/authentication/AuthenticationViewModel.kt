package com.ps.wefriends.presentation.screens.authentication

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ps.wefriends.domain.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth, private val userInfo: DataStore<UserInfo>
) : ViewModel() {

    private var _state = MutableStateFlow(AuthenticationUiState())
    val state = _state.asStateFlow()

//    private var _isGoogleLoading = MutableStateFlow(false)
//    val isGoogleLoading = _isGoogleLoading.asStateFlow()
//
//    private var _isGuestLoading = MutableStateFlow(false)
//    val isGuestLoading = _isGuestLoading.asStateFlow()
//
//    private var _isAuthenticated = MutableStateFlow(false)
//    val isAuthenticated = _isAuthenticated.asStateFlow()
//
//    private var _requireOnboarding = MutableStateFlow<Boolean?>(null)
//    val requireOnboarding = _requireOnboarding.asStateFlow()

    init {
        setRequiredOnboarding()
    }

    private fun setRequiredOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            userInfo.data.collectLatest { info ->
                _state.update {
                    it.copy(
                        isOnboardingRequired = info.showOnboarding
                    )
                }
            }
        }
    }

    fun setGoogleLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                isGoogleLoading = isLoading
            )
        }
    }

    fun setGuestLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                isGuestLoading = isLoading
            )
        }
    }

    fun setAuthenticated(isAuthenticated: Boolean) {
        _state.update {
            it.copy(
                isAuthenticated = isAuthenticated
            )
        }
    }

    fun signInAsGuest(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        firebaseAuth.signInAnonymously().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                onSuccess()
            } else {
                result.exception?.let { onError(it) }
            }
        }
    }
}