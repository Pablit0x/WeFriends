package com.ps.wefriends.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ps.wefriends.BuildConfig
import com.ps.wefriends.navigation.NavGraph
import com.ps.wefriends.navigation.Screen
import com.ps.wefriends.presentation.core.WeFriendsTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTimber()
        setContent {
            val startDestination = getStartDestination()
            val navController = rememberNavController()
            WeFriendsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        startDestinationRoute = startDestination, navController = navController
                    )
                }
            }
        }
    }
}

private fun getStartDestination(): String {
    // TODO:
    // Logged in --> Home Screen
    // Not Logged in --> Authentication Screen
    return Screen.Authentication.route
}

private fun setupTimber() {
    if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    }

}
