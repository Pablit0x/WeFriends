package com.ps.wefriends.presentation.screens.home

import com.ps.wefriends.domain.model.Survey
import com.ps.wefriends.presentation.screens.authentication.UserData

data class HomeState(
    val currentUser: UserData? = null,
    val surveys: List<Survey> = emptyList(),
    val isDataLoading: Boolean = true,
    val error: Exception? = null,
    val isSearchViewOpen: Boolean = false,
    val isFilterViewOpen: Boolean = false,
    val isSignOutDialogOpen: Boolean = false
)
