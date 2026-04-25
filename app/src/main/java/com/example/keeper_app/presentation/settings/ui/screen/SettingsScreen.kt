package com.example.keeper_app.presentation.settings.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.custom.CustomAppBar
import com.example.keeper_app.presentation.ui.theme.custom.CustomStatusBar

private object SettingsScreenStrings{
    val title = R.string.settings_title
    val backTitle = R.string.back_btn
    val editPinTitle = R.string.settings_change_pin_bth
    val editPasswordTitle = R.string.settings_change_password_bth
    val logOutTitle = R.string.settings_logout_bth
    val removeAccountTitle = R.string.settings_del_bth
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
//    navController: NavController,
){
    val context = LocalContext.current
    SettingsContent(onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    onBack: () -> Unit,
){
    CustomStatusBar()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues()
                    .calculateTopPadding()
            ),
        topBar = {
            CustomAppBar(
                text = stringResource(SettingsScreenStrings.title),
                icon = painterResource(R.drawable.ic_arrow_back),
                iconText = stringResource(SettingsScreenStrings.backTitle),
                onClick = onBack
            )
        },
    )
    { paddingValue ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(vertical =  dimensionResource(R.dimen.padding_body))
        ){
            SettingsItem(
                title = stringResource(SettingsScreenStrings.editPinTitle),
                onClick = {}
            )
            SettingsItem(
                title = stringResource(SettingsScreenStrings.editPasswordTitle),
                onClick = {}
            )
            SettingsItem(
                title = stringResource(SettingsScreenStrings.logOutTitle),
                onClick = {}
            )
            SettingsItem(
                title = stringResource(SettingsScreenStrings.removeAccountTitle),
                onClick = {}
            )
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    onClick: () -> Unit
){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_body))
                .clickable{
                    onClick()
                },
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.indent_medium_super))
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsContentPreview(){
    AppTheme {
        SettingsContent(
            onBack = {}
        )
    }
}