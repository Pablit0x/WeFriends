package com.ps.wefriends.navigation

sealed class Screen(val route: String) {
    data object Authentication : Screen(route = Routes.AUTHENTICATION)
    data object Onboarding : Screen(route = Routes.ONBOARDING)
    data object Home : Screen(route = Routes.HOME)
    data object CreateSurvey : Screen(route = Routes.CREATE_SURVEY)
}