package com.ps.wefriends.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ps.wefriends.R
import com.ps.wefriends.presentation.components.CustomAlertDialog
import com.ps.wefriends.presentation.screens.authentication.AuthenticationScreen
import com.ps.wefriends.presentation.screens.authentication.AuthenticationViewModel
import com.ps.wefriends.presentation.screens.create_survey.CreateSurveyScreen
import com.ps.wefriends.presentation.screens.create_survey.CreateSurveyViewModel
import com.ps.wefriends.presentation.screens.home.HomeScreen
import com.ps.wefriends.presentation.screens.home.HomeViewModel
import com.ps.wefriends.presentation.screens.onboarding.OnboardingScreen
import com.ps.wefriends.presentation.screens.onboarding.OnboardingViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavGraph(startDestinationRoute: String, navController: NavHostController) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        authenticationScreen(navigateHome = {
            navController.navigate(Screen.Home.route)
        }, navigateOnboarding = { navController.navigate(Screen.Onboarding.route) })
        onboardingScreen(navigateHome = {
            navController.navigate(Screen.Home.route)
        })
        homeScreen(navigateAuth = {
            navController.navigate(Screen.Authentication.route)
        }, navigateCreateSurvey = {
            navController.navigate(Screen.CreateSurvey.route)
        })
        createSurveyScreen(navigateHome = {
            navController.navigate(Screen.Home.route)
        })
    }
}

fun NavGraphBuilder.authenticationScreen(navigateHome: () -> Unit, navigateOnboarding: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val viewModel = hiltViewModel<AuthenticationViewModel>()
        val context = LocalContext.current
        val firebaseAuth = viewModel.firebaseAuth
        val scope = rememberCoroutineScope()
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val state by viewModel.state.collectAsStateWithLifecycle()

        AuthenticationScreen(
            state = state,
            firebaseAuth = firebaseAuth,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            onGuestSignInClicked = {
                viewModel.setGuestLoading(isLoading = true)
                viewModel.signInAsGuest(onSuccess = {
                    scope.launch {
                        messageBarState.addSuccess(message = context.getString(R.string.successful_sign_in))
                        delay(1500)
                        viewModel.setAuthenticated(isAuthenticated = true)
                        viewModel.setGuestLoading(isLoading = false)
                    }
                }, onError = { exception ->
                    messageBarState.addError(exception)
                    viewModel.setGuestLoading(isLoading = false)
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
            },
            navigateHome = navigateHome,
            navigateOnboarding = navigateOnboarding
        )
    }
}

fun NavGraphBuilder.onboardingScreen(navigateHome: () -> Unit) {
    composable(route = Screen.Onboarding.route) {
        val viewModel = hiltViewModel<OnboardingViewModel>()
        OnboardingScreen(setAsCompleted = {
            viewModel.setOnboardingAsCompleted()
            navigateHome()
        })
    }
}

fun NavGraphBuilder.homeScreen(navigateAuth: () -> Unit, navigateCreateSurvey: () -> Unit) {
    composable(route = Screen.Home.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        HomeScreen(state = state, drawerState = drawerState, onOpenDrawerIconClicked = {
            scope.launch {
                drawerState.open()
            }
        }, onSignOutClicked = {
            scope.launch {
                drawerState.close()
                viewModel.openSignOutDialog()
            }
        }, addSurveyClicked = navigateCreateSurvey)

        CustomAlertDialog(title = stringResource(id = R.string.sign_out),
            message = stringResource(id = R.string.sign_out_message),
            isOpen = state.isSignOutDialogOpen,
            onCloseDialog = {
                scope.launch {
                    drawerState.open()
                    viewModel.closeSignOutDialog()
                }
            },
            onConfirmClicked = {
                viewModel.signOut()
                navigateAuth()
            })
    }
}

fun NavGraphBuilder.createSurveyScreen(navigateHome: () -> Unit) {
    composable(route = Screen.CreateSurvey.route) {
        val viewModel = hiltViewModel<CreateSurveyViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        CreateSurveyScreen(
            state = state,
            onAddSurveyClicked = viewModel::addSurvey,
            onTitleChanged = viewModel::onTitleTextChanged,
            navigateHome = navigateHome
        )
    }
}