@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.ps.wefriends.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.ps.wefriends.presentation.screens.authentication.AuthenticationEffect
import com.ps.wefriends.presentation.screens.authentication.AuthenticationEvent
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
        val authClient = viewModel.authClient
        val messageBarState = rememberMessageBarState()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == ComponentActivity.RESULT_OK) {
                        authClient.googleSignInWithIntent(intent = result.data
                            ?: return@rememberLauncherForActivityResult,
                            onSuccess = {
                                viewModel.onEvent(
                                    AuthenticationEvent.OnAuthenticationChange(
                                        isAuthenticated = true
                                    )
                                )
                                viewModel.onEvent(
                                    AuthenticationEvent.OnGoogleLoadingChange(
                                        isLoading = false
                                    )
                                )
                            },
                            onError = {
                                viewModel.onEvent(
                                    AuthenticationEvent.OnShowErrorMessage(
                                        exception = it
                                    )
                                )
                                viewModel.onEvent(
                                    AuthenticationEvent.OnAuthenticationChange(
                                        isAuthenticated = false
                                    )
                                )
                                viewModel.onEvent(
                                    AuthenticationEvent.OnGoogleLoadingChange(
                                        isLoading = false
                                    )
                                )
                            })
                    }
                })

        LaunchedEffect(Unit) {
            viewModel.onEvent(AuthenticationEvent.OnCheckWhetherOnboardingIsRequired)
        }

        LaunchedEffect(effect) {
            when (effect) {
                AuthenticationEffect.OnNavigateHome -> navigateHome()
                AuthenticationEffect.OnNavigateOnboarding -> navigateOnboarding()
                AuthenticationEffect.OnSignInWithGoogleClicked -> {
                    viewModel.onEvent(AuthenticationEvent.OnGoogleLoadingChange(isLoading = true))
                    val signInIntentSender = authClient.googleSignIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@LaunchedEffect
                        ).build()
                    )
                }

                is AuthenticationEffect.OnShowErrorMessage -> {
                    val exception = (effect as AuthenticationEffect.OnShowErrorMessage).exception
                    messageBarState.addError(exception = exception)
                }

                is AuthenticationEffect.OnShowSuccessMessage -> {
                    val message = (effect as AuthenticationEffect.OnShowSuccessMessage).message
                    messageBarState.addSuccess(message = message)
                }


                null -> {}
            }
        }

        AuthenticationScreen(state = state,
            messageBarState = messageBarState,
            onGuestSignInClicked = {
                viewModel.onEvent(AuthenticationEvent.OnGuestLoadingChange(isLoading = true))
                viewModel.onEvent(AuthenticationEvent.OnSignInAsGuestClicked(onSuccess = {
                    viewModel.onEvent(
                        AuthenticationEvent.OnShowSuccessMessage(
                            message = context.getString(
                                R.string.successful_sign_in
                            )
                        )
                    )
                    viewModel.onEvent(AuthenticationEvent.OnAuthenticationChange(isAuthenticated = true))
                    viewModel.onEvent(AuthenticationEvent.OnGuestLoadingChange(isLoading = false))
                }, onError = {
                    viewModel.onEvent(AuthenticationEvent.OnShowErrorMessage(it))
                    viewModel.onEvent(AuthenticationEvent.OnAuthenticationChange(isAuthenticated = false))
                }))
            },
            onGoogleSignInClicked = {
                viewModel.onEvent(AuthenticationEvent.OnSignInWithGoogle)
            },
            navigateHome = {
                viewModel.onEvent(AuthenticationEvent.OnNavigateHome)
            },
            navigateOnboarding = {
                viewModel.onEvent(AuthenticationEvent.OnNavigateOnboarding)
            })
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
        val currentUser = viewModel.googleAuthClient.getSignedInUser()
        val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val bottomSheetState = rememberModalBottomSheetState()

        LaunchedEffect(currentUser) {
            viewModel.onEvent(HomeEvent.OnSetCurrentUser(userData = currentUser))
        }

        LaunchedEffect(Unit) {
            viewModel.onEvent(HomeEvent.OnGetSurveys)
        }

        LaunchedEffect(drawerState.isOpen) {
            viewModel.onEvent(HomeEvent.OnNavigationDrawerChange(drawerState.isOpen))
        }

        LaunchedEffect(effect) {
            when (effect) {
                HomeEffect.OnNavigateAuth -> navigateAuth()
                HomeEffect.OnNavigateToCreateSurvey -> navigateCreateSurvey()
                is HomeEffect.OnNavigationDrawerChange -> {
                    val isOpen = (effect as HomeEffect.OnNavigationDrawerChange).isOpen
                    if (isOpen) drawerState.open() else drawerState.close()
                }

                else -> {}
            }
        }

        HomeScreen(state = state,
            drawerState = drawerState,
            bottomSheetState = bottomSheetState,
            onOpenNavigationDrawer = {
                viewModel.onEvent(HomeEvent.OnNavigationDrawerChange(isOpen = true))
            },
            onSignOutClicked = {
                viewModel.onEvent(HomeEvent.OnSignOutDialogChange(isOpen = true))
                viewModel.onEvent(HomeEvent.OnNavigationDrawerChange(isOpen = false))
            },
            addSurveyClicked = {
                viewModel.onEvent(HomeEvent.OnAddPressed)
            },
            onOpenFilterView = {
                viewModel.onEvent(HomeEvent.OnFilterChange(isOpen = true))
            },
            onCloseFilterView = {
                viewModel.onEvent(HomeEvent.OnFilterChange(isOpen = false))
            },
            onOpenSearchView = {
                viewModel.onEvent(HomeEvent.OnSearchChange(isOpen = true))
            },
            onCloseSearchView = {
                viewModel.onEvent(HomeEvent.OnSearchChange(isOpen = false))
            })

        CustomAlertDialog(title = stringResource(id = R.string.sign_out),
            message = stringResource(id = R.string.sign_out_message),
            isOpen = state.isSignOutDialogOpen,
            onCloseDialog = {
                viewModel.onEvent(HomeEvent.OnNavigationDrawerChange(isOpen = true))
                viewModel.onEvent(HomeEvent.OnSignOutDialogChange(isOpen = false))
            },
            onConfirmClicked = {
                viewModel.onEvent(HomeEvent.OnFirebaseSignOut)
                viewModel.onEvent(HomeEvent.OnNavigateAuth)
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