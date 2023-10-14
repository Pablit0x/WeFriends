@file:OptIn(ExperimentalMaterial3Api::class)

package com.ps.wefriends.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ps.wefriends.presentation.screens.home.HomeEffect
import com.ps.wefriends.presentation.screens.home.HomeEvent
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
        val currentUser = viewModel.auth.currentUser
        val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val bottomSheetState = rememberModalBottomSheetState()

        LaunchedEffect(currentUser) {
            viewModel.onEvent(HomeEvent.OnSetCurrentUser(currentUser = currentUser))
        }

        LaunchedEffect(Unit) {
            viewModel.onEvent(HomeEvent.OnGetSurveys)
        }

        LaunchedEffect(effect) {
            when (effect) {
                HomeEffect.OnCloseDrawer -> drawerState.close()
                HomeEffect.OnNavigateAuth -> navigateAuth()
                HomeEffect.OnNavigateToCreateSurvey -> navigateCreateSurvey()
                HomeEffect.OnOpenDrawer -> drawerState.open()
                else -> {}
            }
        }

        HomeScreen(state = state,
            drawerState = drawerState,
            bottomSheetState = bottomSheetState,
            onOpenDrawerIconClicked = {
                viewModel.onEvent(HomeEvent.OnOpenDrawer)
            },
            onSignOutClicked = {
                viewModel.onEvent(HomeEvent.OnSignOutClicked)
            },
            addSurveyClicked = {
                viewModel.onEvent(HomeEvent.OnAddPressed)
            },
            onOpenFilterView = {
                viewModel.onEvent(HomeEvent.OnOpenFilter)
            },
            onCloseFilterView = {
                viewModel.onEvent(HomeEvent.OnCloseFilter)
            },
            onOpenSearchView = {
                viewModel.onEvent(HomeEvent.OnOpenSearch)
            },
            onCloseSearchView = {
                viewModel.onEvent(HomeEvent.OnCloseSearch)
            })

        CustomAlertDialog(title = stringResource(id = R.string.sign_out),
            message = stringResource(id = R.string.sign_out_message),
            isOpen = state.isSignOutDialogOpen,
            onCloseDialog = {
                viewModel.onEvent(HomeEvent.OnSignOutCanceled)
            },
            onConfirmClicked = {
                viewModel.onEvent(HomeEvent.OnSignOutConfirmed)
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