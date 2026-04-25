package com.example.keeper_app.presentation.about.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.custom.CustomAppBar
import com.example.keeper_app.presentation.ui.theme.custom.CustomStatusBar

private object AboutScreenStrings{
    val title = R.string.about_title
    val backTitle = R.string.back_btn
    val description = R.string.about_desc
    val version = R.string.about_version_title
    val emailTitle = R.string.about_email_title
    val emailValue = R.string.about_email_value
    val developerTitle = R.string.about_developer_title
}

@Composable
fun AboutScreen (
    version: String,
    onBack: () -> Unit
){
    AboutContent(
        version = version,
        onBack = onBack
    )
}

@Composable
fun AboutContent(
    version: String,
    onBack: ()-> Unit
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
                text = stringResource(AboutScreenStrings.title),
                icon = painterResource(R.drawable.ic_arrow_back),
                iconText = stringResource(AboutScreenStrings.backTitle),
                onClick = onBack
            )
        },
    )
    { paddingValue ->
        Column (
            modifier = Modifier
                .padding(paddingValue)
                .padding(dimensionResource(R.dimen.padding_body))
        ){
            Text(
                text = stringResource(AboutScreenStrings.description)
            )
            CustomSpacer()
            Row {
                Text(
                    text = "${stringResource(AboutScreenStrings.version)}:"
                )

                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))

                Text(
                    text = version,
                    fontWeight = FontWeight.Bold
                )
            }
            CustomSpacer()
            Row {
                Text(
                    text = "${stringResource(AboutScreenStrings.emailTitle)}:"
                )

                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))

                Text(
                    text = stringResource(AboutScreenStrings.emailValue),
                    fontWeight = FontWeight.Bold
                )
            }
            CustomSpacer()
            Row {
                Text(
                    text = "${stringResource(R.string.about_developer_title)}:"
                )

                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))

                Text(
                    text = stringResource(AboutScreenStrings.developerTitle),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CustomSpacer(){
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = dimensionResource(R.dimen.indent_medium_super))
        .height(1.dp)
        .background(MaterialTheme.colorScheme.outline)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AboutContentPreview(){
    AppTheme {
        AboutContent(
            version = "1.0.1",
            onBack = { }
        )
    }
}




