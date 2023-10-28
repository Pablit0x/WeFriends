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
import com.ps.wefriends.presentation.screens.create_survey.CreateSurveyEffect
import com.ps.wefriends.presentation.screens.create_survey.CreateSurveyEvent
import com.ps.wefriends.presentation.screens.create_survey.CreateSurveyScreen
import com.ps.wefriends.presentation.screens.create_survey.CreateSurveyViewModel
import com.ps.wefriends.presentation.screens.home.HomeEffect
import com.ps.wefriends.presentation.screens.home.HomeEvent
import com.ps.wefriends.presentation.screens.home.HomeScreen
import com.ps.wefriends.presentation.screens.home.HomeViewModel
import com.ps.wefriends.presentation.screens.onboarding.OnboardingEffect
import com.ps.wefriends.presentation.screens.onboarding.OnboardingEvent
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
        }, navigateAuth = {
            navController.navigate(Screen.Authentication.route)
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
                                    event = AuthenticationEvent.OnAuthenticationChange(
                                        isAuthenticated = true
                                    )
                                )
                                viewModel.onEvent(
                                    event = AuthenticationEvent.OnGoogleLoadingChange(
                                        isLoading = false
                                    )
                                )
                            },
                            onError = {
                                viewModel.onEvent(
                                    event = AuthenticationEvent.OnShowErrorMessage(
                                        exception = it
                                    )
                                )
                                viewModel.onEvent(
                                    event = AuthenticationEvent.OnAuthenticationChange(
                                        isAuthenticated = false
                                    )
                                )
                                viewModel.onEvent(
                                    event = AuthenticationEvent.OnGoogleLoadingChange(
                                        isLoading = false
                                    )
                                )
                            })
                    }
                })

        LaunchedEffect(Unit) {
            viewModel.onEvent(event = AuthenticationEvent.OnCheckWhetherOnboardingIsRequired)
        }

        LaunchedEffect(effect) {
            when (effect) {
                AuthenticationEffect.OnNavigateHome -> navigateHome()
                AuthenticationEffect.OnNavigateOnboarding -> navigateOnboarding()
                AuthenticationEffect.OnSignInWithGoogleClicked -> {
                    viewModel.onEvent(event = AuthenticationEvent.OnGoogleLoadingChange(isLoading = true))
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
                viewModel.onEvent(event = AuthenticationEvent.OnGuestLoadingChange(isLoading = true))
                viewModel.onEvent(event = AuthenticationEvent.OnSignInAsGuestClicked(onSuccess = {
                    viewModel.onEvent(
                        event = AuthenticationEvent.OnAuthenticationChange(
                            isAuthenticated = true
                        )
                    )
                    viewModel.onEvent(event = AuthenticationEvent.OnGuestLoadingChange(isLoading = false))
                }, onError = {
                    viewModel.onEvent(event = AuthenticationEvent.OnShowErrorMessage(it))
                    viewModel.onEvent(
                        event = AuthenticationEvent.OnAuthenticationChange(
                            isAuthenticated = false
                        )
                    )
                }))
            },
            onGoogleSignInClicked = {
                viewModel.onEvent(event = AuthenticationEvent.OnSignInWithGoogle)
            },
            navigateHome = {
                viewModel.onEvent(event = AuthenticationEvent.OnNavigateHome)
            },
            navigateOnboarding = {
                viewModel.onEvent(event = AuthenticationEvent.OnNavigateOnboarding)
            })
    }
}

fun NavGraphBuilder.onboardingScreen(navigateHome: () -> Unit, navigateAuth: () -> Unit) {
    composable(route = Screen.Onboarding.route) {
        val viewModel = hiltViewModel<OnboardingViewModel>()
        val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

        LaunchedEffect(effect) {
            when (effect) {
                OnboardingEffect.OnNavigateAuth -> navigateAuth()
                OnboardingEffect.OnNavigateHome -> navigateHome()
                null -> {}
            }
        }

        OnboardingScreen(setAsCompleted = {
            viewModel.onEvent(event = OnboardingEvent.OnCompleteOnboarding)
            viewModel.onEvent(event = OnboardingEvent.OnNavigateHome)
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

                null -> {}
            }
        }

        HomeScreen(state = state,
            drawerState = drawerState,
            bottomSheetState = bottomSheetState,
            onOpenNavigationDrawer = {
                viewModel.onEvent(event = HomeEvent.OnNavigationDrawerChange(isOpen = true))
            },
            onSignOutClicked = {
                viewModel.onEvent(event = HomeEvent.OnSignOutDialogChange(isOpen = true))
                viewModel.onEvent(event = HomeEvent.OnNavigationDrawerChange(isOpen = false))
            },
            addSurveyClicked = {
                viewModel.onEvent(event = HomeEvent.OnAddPressed)
            },
            onOpenFilterView = {
                viewModel.onEvent(event = HomeEvent.OnFilterChange(isOpen = true))
            },
            onCloseFilterView = {
                viewModel.onEvent(event = HomeEvent.OnFilterChange(isOpen = false))
            },
            onOpenSearchView = {
                viewModel.onEvent(event = HomeEvent.OnSearchChange(isOpen = true))
            },
            onCloseSearchView = {
                viewModel.onEvent(event = HomeEvent.OnSearchChange(isOpen = false))
            })

        CustomAlertDialog(title = stringResource(id = R.string.sign_out),
            message = stringResource(id = R.string.sign_out_message),
            isOpen = state.isSignOutDialogOpen,
            onCloseDialog = {
                viewModel.onEvent(event = HomeEvent.OnNavigationDrawerChange(isOpen = true))
                viewModel.onEvent(event = HomeEvent.OnSignOutDialogChange(isOpen = false))
            },
            onConfirmClicked = {
                viewModel.onEvent(event = HomeEvent.OnFirebaseSignOut)
                viewModel.onEvent(event = HomeEvent.OnNavigateAuth)
            })
    }
}

fun NavGraphBuilder.createSurveyScreen(navigateHome: () -> Unit) {
    composable(route = Screen.CreateSurvey.route) {
        val viewModel = hiltViewModel<CreateSurveyViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

        LaunchedEffect(key1 = effect) {
            when (effect) {
                CreateSurveyEffect.OnNavigateHome -> navigateHome()
                null -> {}
            }
        }

        LaunchedEffect(Unit) {
            viewModel.onEvent(event = CreateSurveyEvent.GetSignedUser)
        }

        CreateSurveyScreen(state = state,
            onCreateSurveyClicked = { viewModel.onEvent(event = CreateSurveyEvent.CreateNewSurvey) },
            onTitleChanged = { title ->
                viewModel.onEvent(event = CreateSurveyEvent.OnTitleChanged(title))
            },
            navigateHome = {
                viewModel.onEvent(event = CreateSurveyEvent.OnNavigateHome)
            },
            onImageSelected = {
                viewModel.onEvent(event = CreateSurveyEvent.OnUploadImageToFirebaseStorage(imageUri = it))
            })
    }
}