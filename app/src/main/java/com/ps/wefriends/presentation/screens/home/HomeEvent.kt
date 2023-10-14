package com.ps.wefriends.presentation.screens.home

import com.google.firebase.auth.FirebaseUser

sealed class HomeEvent {
    data class OnSetCurrentUser(val currentUser: FirebaseUser?) : HomeEvent()
    data object OnGetSurveys : HomeEvent()
    data class OnFilterChange(val isOpen: Boolean) : HomeEvent()
    data class OnSearchChange(val isOpen : Boolean) : HomeEvent()
    data class OnNavigationDrawerChange(val isOpen: Boolean) : HomeEvent()
    data class OnSignOutDialogChange(val isOpen: Boolean) : HomeEvent()
    data object OnFirebaseSignOut : HomeEvent()
    data object OnAddPressed : HomeEvent()
    data object OnNavigateAuth : HomeEvent()
}
