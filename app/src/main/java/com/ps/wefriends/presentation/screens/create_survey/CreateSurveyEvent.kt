package com.ps.wefriends.presentation.screens.create_survey

import android.net.Uri

sealed class CreateSurveyEvent{
    data object CreateNewSurvey: CreateSurveyEvent()
    data object GetSignedUser: CreateSurveyEvent()
    data class OnTitleChanged(val text : String) : CreateSurveyEvent()
    data class OnImageChanged(val imageUri: Uri?) : CreateSurveyEvent()
    data object OnUploadImageToFirebaseStorage : CreateSurveyEvent()
    data object OnNavigateHome : CreateSurveyEvent()
}

