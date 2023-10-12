package com.ps.wefriends.presentation.screens.home

import com.google.firebase.auth.FirebaseUser
import com.ps.wefriends.domain.model.Survey

data class HomeUiState(
    val currentUser: FirebaseUser? = null,
    val surveys: List<Survey> = emptyList(),
    val isLoading: Boolean = true,
    val error: Exception? = null,
    val isSearchActive: Boolean = false,
    val isFilterActive: Boolean = false,
    val isSignOutDialogOpen: Boolean = false
)
