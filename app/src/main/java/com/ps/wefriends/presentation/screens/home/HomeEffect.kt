package com.ps.wefriends.presentation.screens.home

sealed class HomeEffect {
    data object OnNavigateToCreateSurvey : HomeEffect()
    data object OnOpenDrawer : HomeEffect()
    data object OnNavigateAuth : HomeEffect()
    data object OnCloseDrawer : HomeEffect()
}