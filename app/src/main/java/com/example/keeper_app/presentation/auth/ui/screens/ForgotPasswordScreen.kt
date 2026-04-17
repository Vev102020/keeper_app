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
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keeper_app.R
import com.example.keeper_app.presentation.auth.ui.components.EmailTextField
import com.example.keeper_app.presentation.auth.viewmodel.AuthViewModel
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors
import com.example.keeper_app.presentation.ui.theme.custom.ButtonStyle
import com.example.keeper_app.presentation.ui.theme.custom.CustomLoadingButton
import com.example.keeper_app.presentation.ui.theme.custom.CustomStatusBar
import com.example.keeper_app.presentation.ui.theme.custom.TextLink

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.forgotPasswordState.collectAsState()

    ForgotPasswordContent(
        email = state.email,
        onEmailChange = { viewModel.updateForgotPasswordState(email = it) },
        isLoading = state.isLoading,
        error = state.error,
        successMessage = state.successMessage,
        onSendResetLink = {
            viewModel.sendPasswordResetEmail(state.email)
        },
        onBack = onBack
    )
}

@Composable
private fun ForgotPasswordContent(
    email: String,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    successMessage: String?,
    onSendResetLink: () -> Unit,
    onBack: () -> Unit,
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
                        text = stringResource(R.string.forgot_title),
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

                    error?.let { error ->
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_small_super)))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    successMessage?.let { success ->
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_small_super)))
                        Text(
                            text = success,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

                    CustomLoadingButton(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = {
                            if (!isLoading) onSendResetLink()
                        },
                        isLoading = isLoading,
                        style = ButtonStyle.Primary,
                        text = stringResource(R.string.forgot_btn),
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.forgot_link_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.LocalColors.link.text
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))
                    TextLink(
                        text = stringResource(R.string.forgot_link_page),
                        onClick = onBack,
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
private fun ForgotPasswordContentPreview() {
    AppTheme {
        ForgotPasswordContent(
            email = "test@mail.ru",
            onEmailChange = {},
            isLoading = false,
            error = null,
            successMessage = null,
            onSendResetLink = {},
            onBack = {},
        )
    }
}
