package com.ps.wefriends.presentation.screens.onboarding

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.wefriends.domain.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val userInfoDataStore: DataStore<UserInfo>) :
    ViewModel() {
    fun setOnboardingAsCompleted() {
        viewModelScope.launch {
            userInfoDataStore.updateData { userInfo ->
                userInfo.copy(
                    showOnboarding = false
                )
            }
        }
    }

}