package com.ps.wefriends.presentation.screens.create_survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateSurveyScreen(
    state: CreateSurveyUiState,
    onAddSurveyClicked: () -> Unit,
    onTitleChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = state.title, onValueChange = onTitleChanged)

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onAddSurveyClicked) {
            Text(text = "Create")
        }
    }
}