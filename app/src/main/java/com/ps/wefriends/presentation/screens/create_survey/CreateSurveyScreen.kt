package com.ps.wefriends.presentation.screens.create_survey

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ps.wefriends.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSurveyScreen(
    state: CreateSurveyState,
    onCreateSurveyClicked: () -> Unit,
    onTitleChanged: (String) -> Unit,
    navigateHome: () -> Unit,
    onImageSelected: (Uri) -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = stringResource(id = R.string.create_survet_screen_title))
        }, navigationIcon = {
            IconButton(onClick = navigateHome) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigate Home")
            }
        })
    }) { padding ->
        CreateSurveyContent(
            state = state,
            onTitleChanged = onTitleChanged,
            onAddSurveyClicked = onCreateSurveyClicked,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            onImageSelected = {
                onImageSelected(it)
            }
        )
    }
}