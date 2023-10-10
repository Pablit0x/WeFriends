package com.ps.wefriends.presentation.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ps.wefriends.presentation.components.FormPreviewItemShimmer

@Composable
fun HomeContent() {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 16.dp,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(20) {
            FormPreviewItemShimmer(
                isLoading = true,
                contentAfterLoading = { /*TODO*/ },
            )
        }
    }
}