package com.ps.wefriends.presentation.components

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ps.wefriends.domain.model.Survey
import com.ps.wefriends.domain.repository.SurveysResponse
import com.ps.wefriends.util.Response

@Composable
fun SurveysList(
    surveysResponse: SurveysResponse, modifier: Modifier = Modifier, numOfColumns: Int = 2
) {
    val context = LocalContext.current
    var surveys by remember { mutableStateOf(emptyList<Survey>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(surveysResponse) {
        when (surveysResponse) {
            Response.Loading -> isLoading = true
            is Response.Success -> {
                isLoading = false
                surveys = surveysResponse.data
            }

            is Response.Failure -> {
                Toast.makeText(
                    context, "Error occurred: ${surveysResponse.e?.message}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    if (surveys.isEmpty() && !isLoading) {
        EmptySurveyList(title = "Test", description = "test 1234", onRetryAction = {})
    } else {
        LazyVerticalStaggeredGrid(
            modifier = modifier,
            columns = StaggeredGridCells.Fixed(numOfColumns),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp)
        ) {
            if (isLoading) {
                items(8) {
                    SurveyShimmerItem()
                }
            } else {
                itemsIndexed(surveys) { index, survey ->
                    Text(text = "Index = $index, Survey = $survey")
                }
            }
        }
    }
}