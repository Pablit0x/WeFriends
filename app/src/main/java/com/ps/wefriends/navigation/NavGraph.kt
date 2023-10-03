package com.ps.wefriends.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(startDestinationRoute: String, navController: NavHostController) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
    }
}

fun NavGraphBuilder.authenticationScreen(){
    composable(route = Screen.Authentication.route){

    }
}

fun NavGraphBuilder.onboardingScreen(){
    composable(route = Screen.Onboarding.route){

    }
}

fun NavGraphBuilder.homeScreen(){
    composable(route = Screen.Home.route){

    }
}