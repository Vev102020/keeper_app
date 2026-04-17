package com.example.keeper_app.presentation.auth.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keeper_app.R
import com.example.keeper_app.presentation.auth.navigate.AuthNav
import com.example.keeper_app.presentation.auth.ui.components.EmailTextField
import com.example.keeper_app.presentation.auth.ui.components.PasswordTextField
import com.example.keeper_app.presentation.auth.viewmodel.AuthViewModel
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors
import com.example.keeper_app.presentation.ui.theme.custom.ButtonStyle
import com.example.keeper_app.presentation.ui.theme.custom.CustomLoadingButton
import com.example.keeper_app.presentation.ui.theme.custom.CustomStatusBar
import com.example.keeper_app.presentation.ui.theme.custom.TextLink

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
){
    val loginState by viewModel.loginState.collectAsState()

    LoginContent(
        email = loginState.email,
        onEmailChange = {viewModel.updateLoginState(email = it)},
        password = loginState.password,
        onPasswordChange = {viewModel.updateLoginState(password = it)},
        showPassword = loginState.showPassword,
        onShowPasswordClick = {viewModel.toggleShowPassword()},
        isLoading = loginState.isLoading,
        error = loginState.error,
        onLoginClick = {viewModel.login(
            email = loginState.email,
            password = loginState.password
        )},
        onForgotPasswordClick = {
            navController.navigate(AuthNav.ForgotPassword.route)
        },
        onRegisterClick = {
            navController.navigate(AuthNav.Register.route)
        },
    )
}

@Composable
private fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit,
    isLoading: Boolean,
    error: String?,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    CustomStatusBar()

    Scaffold(
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars.asPaddingValues()
                .calculateTopPadding()
            ),
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = dimensionResource(R.dimen.padding_body)),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.login_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

                    EmailTextField(
                        email = email,
                        onEmailChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        showLabel = false,
                        placeholder = stringResource(R.string.placeholder_email)
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

                    PasswordTextField(
                        password = password,
                        onPasswordChange = onPasswordChange,
                        showPassword = showPassword,
                        onShowPasswordClick = onShowPasswordClick,
                        modifier = Modifier.fillMaxWidth(),
                        showLabel = false,
                        placeholder = stringResource(R.string.placeholder_password)
                    )

                    error?.let { error ->
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_small_super)))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

                    CustomLoadingButton(
                        modifier = Modifier.width(140.dp),
                        onClick = {
                            if (!isLoading) onLoginClick()
                        },
                        isLoading = isLoading,
                        style = ButtonStyle.Primary,
                        text = stringResource(R.string.login_btn),
                    )


                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

                    TextLink(
                        text = stringResource(R.string.remember_btn),
                        onClick = onForgotPasswordClick,
                        style = MaterialTheme.typography.bodyMedium,
                        textColor = MaterialTheme.LocalColors.link.text
                    )
                }


                Row(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.register_link_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.LocalColors.link.text
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))
                    TextLink(
                        text = stringResource(R.string.register_link_page),
                        onClick = onRegisterClick,
                        style = MaterialTheme.typography.bodyMedium,
                        textColor = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginContentPreview(){
    AppTheme{
        LoginContent(
            email = "test@mail.ru",
            onEmailChange = {},
            password = "1234",
            onPasswordChange = {},
            showPassword = true,
            onShowPasswordClick = {},
            isLoading = false,
            error = null,
            onLoginClick = {},
            onForgotPasswordClick = {},
            onRegisterClick = {},
        )
    }
}