package com.ps.wefriends.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.wefriends.R

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onAccountClicked: () -> Unit,
    onFriendsClicked: () -> Unit,
    onInvitesClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {

                AnimatedBorderCard(
                    modifier = Modifier.fillMaxWidth()
                        .height(250.dp)
                        .padding(all = 30.dp),
                    shape = RoundedCornerShape(15)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = stringResource(id = R.string.app_name))
                }
            }

            NavDrawerItem(
                icon = Icons.Default.AccountCircle,
                contentDescription = "Account",
                text = stringResource(id = R.string.account),
                onClick = onAccountClicked,
                isSelected = false
            )

            NavDrawerItem(
                icon = Icons.Default.PeopleAlt,
                contentDescription = "Friends",
                text = stringResource(id = R.string.friends),
                onClick = onFriendsClicked,
                isSelected = false
            )
            NavDrawerItem(
                icon = Icons.Default.LibraryAddCheck,
                contentDescription = "Invites",
                text = stringResource(id = R.string.invites),
                onClick = onAccountClicked,
                isSelected = false
            )
            NavDrawerItem(
                icon = Icons.Default.Logout,
                contentDescription = "Sign Out",
                text = stringResource(id = R.string.sign_out),
                onClick = onSignOutClicked,
                isSelected = false
            )

        }
    }, content = content)
}