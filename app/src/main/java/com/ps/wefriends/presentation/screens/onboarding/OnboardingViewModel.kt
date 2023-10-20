package com.ps.wefriends.presentation.screens.onboarding

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.wefriends.domain.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val userInfoDataStore: DataStore<UserInfo>) :
    ViewModel() {

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: OnboardingEvent){
        when(event){
            OnboardingEvent.OnCompleteOnboarding -> setOnboardingAsCompleted()
            OnboardingEvent.OnNavigateAuth -> onNavigateAuth()
            OnboardingEvent.OnNavigateHome -> onNavigateHome()
        }
    }


    private fun onNavigateHome(){
        viewModelScope.launch {
            _effect.emit(OnboardingEffect.OnNavigateHome)
        }
    }

    private fun onNavigateAuth(){
        viewModelScope.launch {
            _effect.emit(OnboardingEffect.OnNavigateAuth)
        }
    }
    private fun setOnboardingAsCompleted() {
        viewModelScope.launch {
            userInfoDataStore.updateData { userInfo ->
                userInfo.copy(
                    showOnboarding = false
                )
            }
        }
    }

}