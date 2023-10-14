package com.ps.wefriends.presentation.screens.home

import com.google.firebase.auth.FirebaseUser
import com.ps.wefriends.domain.model.Survey

data class HomeState(
    val currentUser: FirebaseUser? = null,
    val surveys: List<Survey> = emptyList(),
    val isDataLoading: Boolean = true,
    val error: Exception? = null,
    val isSearchViewOpen: Boolean = false,
    val isFilterViewOpen: Boolean = false,
    val isSignOutDialogOpen: Boolean = false
)
