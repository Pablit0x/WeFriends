package com.ps.wefriends.presentation.components

import android.graphics.Bitmap
import androidx.compose.runtime.Composable

@Composable
fun FormPreviewItem() {

}

data class FormPreview(
    val id: String,
    val background: Bitmap? = null,
    val title: String
)