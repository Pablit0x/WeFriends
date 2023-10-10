package com.ps.wefriends.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawerItem(
    icon: ImageVector,
    contentDescription: String,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    hasNews: Boolean = false
) {
    NavigationDrawerItem(label = {
        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }, selected = isSelected, onClick = onClick, badge = {
        if (hasNews) {
            Badge()
        }
    })
}