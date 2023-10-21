package com.ps.wefriends.presentation.screens.create_survey

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.wefriends.R

@Composable
fun CreateSurveyContent(
    state: CreateSurveyState,
    onTitleChanged: (String) -> Unit,
    onAddSurveyClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onImageSelected: (Uri) -> Unit
) {

    val multiplePhotoPicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = 8
        ), onResult = { images ->
            images.forEach { image ->
                onImageSelected(image)
            }
        })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = state.title, onValueChange = onTitleChanged)

        Spacer(modifier = Modifier.height(8.dp))


        Button(onClick = {
            multiplePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }){
            Text(text = stringResource(id = R.string.account))
        }


        Button(onClick = onAddSurveyClicked) {
            Text(text = "Create")
        }
    }
}