package com.example.keeper_app.presentation.auth.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.custom.CustomTextField

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean,
    placeholder: String? = null
){
    val icon =
        if(showPassword)
            painterResource(id = R.drawable.ic_visibility)
        else
            painterResource(id=R.drawable.ic_visibility_off)
    val contentDescription =
        if(showPassword)
            stringResource(id = R.string.show_password)
        else
            stringResource(id = R.string.hide_password)

    CustomTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = if(showLabel) stringResource(R.string.password_hint) else null,
        modifier = modifier,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = onShowPasswordClick, modifier = Modifier.padding(start = 8.dp)) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription
                )
            }
        },
        placeholder = placeholder,
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldPreview(){
    val password = remember { mutableStateOf("test123") }
    var showPassword by remember { mutableStateOf(false) }

    PasswordTextField(
        password = password.value,
        onPasswordChange = {password.value = it},
        showPassword = showPassword,
        onShowPasswordClick = {showPassword = !showPassword},
        showLabel = false,
        placeholder = "Пароль",
    )
}