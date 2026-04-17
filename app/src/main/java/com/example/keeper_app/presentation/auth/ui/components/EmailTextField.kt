package com.example.keeper_app.presentation.auth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.custom.CustomTextField

@Composable
fun EmailTextField (
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean,
    placeholder: String? = null
){
    CustomTextField(
        value = email,
        onValueChange = onEmailChange,
        label = if (showLabel) stringResource(R.string.email_hint) else null,
        modifier = modifier,
        placeholder = placeholder,
    )
}

@Preview(showBackground = true)
@Composable
fun EmailTextFieldPreview(){
    val email = remember { mutableStateOf("test123") }

    EmailTextField(
        email = email.value,
        onEmailChange = {email.value = it},
        showLabel = false,
        placeholder = "Email"
    )
}