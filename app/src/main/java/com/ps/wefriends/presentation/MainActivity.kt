package com.ps.wefriends.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ps.wefriends.BuildConfig
import com.ps.wefriends.navigation.NavGraph
import com.ps.wefriends.navigation.Screen
import com.ps.wefriends.presentation.core.WeFriendsTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT
            )
        )
        setupTimber()
        setContent {
            val startDestination = getStartDestination(auth)
            val navController = rememberNavController()
            WeFriendsTheme {
                NavGraph(
                    startDestinationRoute = startDestination, navController = navController
                )
            }
        }
    }
}

private fun getStartDestination(auth: FirebaseAuth): String {
    return if (auth.currentUser != null) Screen.Home.route else Screen.Authentication.route
}

private fun setupTimber() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    }
}
