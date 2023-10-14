package com.ps.wefriends.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ps.wefriends.R
import com.ps.wefriends.presentation.components.NavigationDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    drawerState: DrawerState,
    bottomSheetState: SheetState,
    onOpenFilterView: () -> Unit,
    onCloseFilterView: () -> Unit,
    onOpenSearchView: () -> Unit,
    onCloseSearchView: () -> Unit,
    onOpenDrawerIconClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    addSurveyClicked: () -> Unit
) {

    NavigationDrawer(
        drawerState = drawerState,
        onAccountClicked = {},
        onFriendsClicked = {},
        onInvitesClicked = {},
        onSignOutClicked = onSignOutClicked
    ) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {

                    IconButton(onClick = {
                        if (state.isSearchViewOpen) onCloseSearchView() else onOpenSearchView()
                    }) {
                        if (state.isSearchViewOpen) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close search view"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Open search view"
                            )
                        }
                    }

                    IconButton(onClick = {
                        if (state.isFilterViewOpen) onCloseFilterView() else onOpenFilterView()
                    }) {
                        if (state.isFilterViewOpen) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close filter view"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Open filter view"
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawerIconClicked) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open navigation drawer"
                        )
                    }
                })
        }, floatingActionButton = {
            FloatingActionButton(onClick = addSurveyClicked) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Open add survey screen")
            }
        }) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                HomeContent(state = state)
            }
        }
    }
}
