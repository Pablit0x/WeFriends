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

    private var _isGoogleLoading = MutableStateFlow(false)
    val isGoogleLoading = _isGoogleLoading.asStateFlow()

    private var _isGuestLoading = MutableStateFlow(false)
    val isGuestLoading = _isGuestLoading.asStateFlow()

    private var _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private var _requireOnboarding = MutableStateFlow<Boolean?>(null)
    val requireOnboarding = _requireOnboarding.asStateFlow()

    init {
        setRequiredOnboarding()
    }

    private fun setRequiredOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            userInfo.data.collectLatest { info ->
                _requireOnboarding.update {
                    info.showOnboarding
                }
            }
        }
    }

    fun setGoogleLoading(isLoading: Boolean) {
        _isGoogleLoading.update { isLoading }
    }

    private fun setGuestLoading(isLoading: Boolean) {
        _isGuestLoading.update { isLoading }
    }

    fun setAuthenticated(isAuthenticated: Boolean) {
        _isAuthenticated.update { isAuthenticated }
    }

    fun signInAsGuest(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        setGuestLoading(isLoading = true)
        firebaseAuth.signInAnonymously().addOnCompleteListener { result ->
            setGuestLoading(isLoading = false)
            if (result.isSuccessful) {
                onSuccess()
            } else {
                result.exception?.let { onError(it) }
            }
        }
    }
}