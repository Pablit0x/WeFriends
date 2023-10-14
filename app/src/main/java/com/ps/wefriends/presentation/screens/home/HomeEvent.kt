package com.ps.wefriends.presentation.screens.home

import com.google.firebase.auth.FirebaseUser

sealed class HomeEvent {

    data class OnSetCurrentUser(val currentUser: FirebaseUser?) : HomeEvent()
    data object OnGetSurveys : HomeEvent()
    data object OnOpenFilter : HomeEvent()
    data object OnCloseFilter : HomeEvent()
    data object OnOpenSearch : HomeEvent()
    data object OnCloseSearch : HomeEvent()
    data object OnOpenDrawer : HomeEvent()
    data object OnCloseDrawer : HomeEvent()
    data object OnSignOutClicked : HomeEvent()
    data object OnOpenSignOutDialog : HomeEvent()
    data object OnCloseSignOutDialog : HomeEvent()
    data object OnSignOutConfirmed : HomeEvent()
    data object OnSignOutCanceled : HomeEvent()
    data object OnAddPressed : HomeEvent()
}
