package com.ps.wefriends.presentation.screens.create_survey

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ps.wefriends.data.workers.UploadWorker
import com.ps.wefriends.domain.repository.SurveysRepository
import com.ps.wefriends.presentation.screens.authentication.AuthUiClient
import com.ps.wefriends.util.Constants
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
class CreateSurveyViewModel @Inject constructor(
    private val authUiClient: AuthUiClient,
    private val repository: SurveysRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _state = MutableStateFlow(CreateSurveyState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CreateSurveyEffect>()
    val effect = _effect.asSharedFlow()



    fun onEvent(event: CreateSurveyEvent){
        when(event){
            CreateSurveyEvent.CreateNewSurvey -> createSurvey()
            CreateSurveyEvent.GetSignedUser -> getSignedUser()
            is CreateSurveyEvent.OnImageChanged -> TODO()
            is CreateSurveyEvent.OnTitleChanged -> onTitleTextChanged(event.text)
            is CreateSurveyEvent.OnUploadImageToFirebaseStorage -> uploadImageToFirebase(event.imageUri)
            CreateSurveyEvent.OnNavigateHome -> onNavigateHome()
        }
    }

    private fun onNavigateHome(){
        viewModelScope.launch {
            _effect.emit(CreateSurveyEffect.OnNavigateHome)
        }
    }

    private fun getSignedUser() {
        _state.update {
            it.copy(
                surveyOwner = authUiClient.getSignedInUser()
            )
        }
    }

    private fun onTitleTextChanged(text: String) {
        _state.update {
            it.copy(
                title = text
            )
        }
    }

    fun createSurvey() {
        state.value.run {
            viewModelScope.launch(Dispatchers.IO) {
                repository.createSurvey(
                    ownerId = surveyOwner!!.userId,
                    title = title,
                    imageUrl = imageUrl,
                    surveyType = surveyType.value,
                    genderAudience = genderAudience.value
                )
            }
        }
    }

    fun uploadImageToFirebase(uri : Uri){
        val request = OneTimeWorkRequestBuilder<UploadWorker>().setInputData(uriInputDataBuilder(uri)).build()
        workManager.enqueue(request)
    }


    private fun uriInputDataBuilder(uri: Uri): Data {
        return Data.Builder().putString(Constants.KEY_IMAGE_URI, uri.toString()).build()
    }
}