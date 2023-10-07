package com.ps.wefriends.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ps.wefriends.R
import com.ps.wefriends.presentation.screens.authentication.AuthenticationScreen
import com.ps.wefriends.presentation.screens.authentication.AuthenticationViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun NavGraph(startDestinationRoute: String, navController: NavHostController) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        authenticationScreen()
        onboardingScreen()
        homeScreen()
    }
}

fun NavGraphBuilder.authenticationScreen() {
    composable(route = Screen.Authentication.route) {
        val viewModel = hiltViewModel<AuthenticationViewModel>()
        val context = LocalContext.current
        val auth = viewModel.auth
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val isGuestLoading by viewModel.isGuestLoading.collectAsStateWithLifecycle()
        val isGoogleLoading by viewModel.isGoogleLoading.collectAsStateWithLifecycle()
        val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()


        AuthenticationScreen(auth = auth,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            isGuestLoading = isGuestLoading,
            isGoogleLoading = isGoogleLoading,
            isAuthenticated = isAuthenticated,
            onGuestSignInClicked = {
                viewModel.signInAsGuest(onSuccess = {
                    messageBarState.addSuccess(message = context.getString(R.string.successful_sign_in))
                }, onError = { exception ->
                    messageBarState.addError(exception)
                })
            },
            onGoogleSignInClicked = {
                oneTapSignInState.open()
                viewModel.setGoogleLoading(isLoading = true)
            },
            onSuccessfulFirebaseSignIn = {
                messageBarState.addSuccess(message = context.getString(R.string.successful_sign_in))
            },
            onFailedFirebaseSignIn = {
                messageBarState.addError(it)
                viewModel.setGoogleLoading(isLoading = false)
            },
            onDialogDismissed = { errorMsg ->
                messageBarState.addError(Exception(errorMsg))
                viewModel.setGoogleLoading(isLoading = false)
            }) {

        }
    }
}

fun NavGraphBuilder.onboardingScreen() {
    composable(route = Screen.Onboarding.route) {

    }
}

fun NavGraphBuilder.homeScreen() {
    composable(route = Screen.Home.route) {

    }
}