package com.ps.wefriends.presentation.screens.home

sealed class HomeEffect {
    data object OnNavigateToCreateSurvey : HomeEffect()
    data class OnNavigationDrawerChange(val isOpen: Boolean) : HomeEffect()
    data object OnNavigateAuth : HomeEffect()
}