package com.ps.wefriends.presentation.screens.create_survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ps.wefriends.domain.use_case.AddSurvey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSurveyViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val addSurvey: AddSurvey
) : ViewModel() {

    private val _state = MutableStateFlow(CreateSurveyUiState())
    val state = _state.asStateFlow()

    private val currentUser = firebaseAuth.currentUser

    fun onTitleTextChanged(text: String) {
        _state.update {
            it.copy(
                title = text
            )
        }
    }

    fun addSurvey() {
        if(currentUser != null){
            viewModelScope.launch(Dispatchers.IO) {
                addSurvey.invoke(
                    ownerId = currentUser.uid,
                    title = state.value.title,
                    imageUrl = state.value.imageUrl,
                    surveyType = state.value.surveyType.value,
                    genderAudience = state.value.genderAudience.value
                )
            }
        }
    }
}