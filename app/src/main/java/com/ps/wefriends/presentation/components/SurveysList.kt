package com.ps.wefriends.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.wefriends.R
import com.ps.wefriends.presentation.screens.home.HomeUiState

@Composable
fun SurveysList(
    state: HomeUiState, modifier: Modifier = Modifier, numOfColumns: Int = 2
) {
    val context = LocalContext.current


    if (state.surveys.isEmpty() && !state.isLoading) {
        EmptySurveyList(title = stringResource(id = R.string.no_surveys),
            description = stringResource(
                id = R.string.nothing_to_display
            ),
            onRetryAction = {})
    } else {
        LazyVerticalStaggeredGrid(
            modifier = modifier,
            columns = StaggeredGridCells.Fixed(numOfColumns),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp)
        ) {
            if (state.isLoading) {
                items(10) {
                    SurveyShimmerItem()
                }
            } else {
                itemsIndexed(state.surveys) { index, survey ->
                    Text(text = "Index = $index, Survey = $survey")
                }
            }
        }
    }
}