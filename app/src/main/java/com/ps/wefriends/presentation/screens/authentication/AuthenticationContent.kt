package com.ps.wefriends.presentation.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.wefriends.R

@Composable
fun AuthenticationContent(
    isGuestLoading: Boolean,
    isGoogleLoading : Boolean,
    onGuestSignInClicked : () -> Unit,
    onGoogleSignInClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth()
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(10f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = stringResource(id = R.string.login_provider_logo)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.welcome_back),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Text(
                    text = stringResource(id = R.string.please_sign_in_to_continue),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f)
                )
            }
            Column(
                modifier = Modifier.weight(3f), verticalArrangement = Arrangement.Bottom
            ) {


                SignInButton(primaryText = stringResource(id = R.string.sign_in_as_a_email),
                    iconRes =  Icons.Default.Person,
                    isLoading = isGuestLoading,
                    onClick = { onGuestSignInClicked() })

                Spacer(modifier = Modifier.height(12.dp))


                SignInButton(primaryText = stringResource(id = R.string.sign_in_with_google),
                    iconRes = R.drawable.google_logo,
                    isLoading = isGoogleLoading,
                    onClick = { onGoogleSignInClicked() })
            }
        }
    }
}