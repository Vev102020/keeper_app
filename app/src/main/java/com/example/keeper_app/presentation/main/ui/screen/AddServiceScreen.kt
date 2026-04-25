package com.example.keeper_app.presentation.main.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.custom.CustomAppBar
import com.example.keeper_app.presentation.ui.theme.custom.CustomStatusBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keeper_app.presentation.main.viewmodel.AddServiceState
import com.example.keeper_app.presentation.main.viewmodel.MainViewModel
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors
import com.example.keeper_app.presentation.ui.theme.custom.ButtonStyle
import com.example.keeper_app.presentation.ui.theme.custom.CustomButton
import com.example.keeper_app.presentation.ui.theme.custom.CustomLoadingButton
import com.example.keeper_app.presentation.ui.theme.custom.CustomTextField
import androidx.core.net.toUri
import com.example.keeper_app.presentation.main.ui.components.requestCameraPermission
import com.example.keeper_app.presentation.main.viewmodel.NavigationEvent
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

data class OtpAuthData(
    val secret: String,
    val issuer: String?
)

fun parseOtpAuth(uri: String): OtpAuthData? {
    if (!uri.startsWith("otpauth://")) return null
    val uriParsed = uri.toUri()
    val secret = uriParsed.getQueryParameter("secret")?.uppercase()
    val issuer = uriParsed.getQueryParameter("issuer")
        ?: uriParsed.getQueryParameter("label")?.split(":")?.get(0)
    return if (secret != null) OtpAuthData(secret, issuer) else null
}

@Composable
fun AddServiceScreen(
    onBack: () -> Unit
){
    AddServiceContent(
        onBack = onBack,
    )
}

@Composable
private fun AddServiceContent(
    viewModel: MainViewModel = hiltViewModel(),
    title: String = stringResource(R.string.service_title),
    backTitle: String = stringResource(R.string.back_btn),
    onBack: () -> Unit,
){
    val context = LocalContext.current
    val state by viewModel.addServiceState.collectAsState()
    val errorMessage = state.errorMessage

    var hasPermission by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
        }
    )

    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            viewModel.handleScanResult(result)
        }
    )

    //  Проверяем текущее состояние разрешения при старте
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        hasPermission = granted
    }

    //  Слушаем команду на сканирование
    LaunchedEffect(viewModel) {
        viewModel.scanIntent.collect {
            // Проверяем разрешение
            if (hasPermission) {
                val options = ScanOptions().apply {
                    setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    setPrompt("Наведите камеру на QR-код")
                    setBeepEnabled(false)
                    setBarcodeImageEnabled(true)
                }
                scanLauncher.launch(options)
            } else {
                // Запрашиваем разрешение
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Слушаем навигацию
    LaunchedEffect(viewModel) {
        viewModel.navigationEvent.collect {
            onBack()
        }
    }

    FormContent(
        state = state,
        title = title,
        backTitle = backTitle,
        onBack = onBack,
        updateServiceName = { viewModel.updateServiceName(it) },
        updateSecretKey = { viewModel.updateSecretKey(it)},
        onOpenScanner = { viewModel.startScan() },
        errorMessage = errorMessage,
        addService = {viewModel.addService()}
    )
}

@Composable
private fun FormContent(
    state: AddServiceState,
    title: String,
    backTitle: String,
    onBack: () -> Unit,
    updateServiceName: (String) -> Unit,
    updateSecretKey: (String) -> Unit,
    onOpenScanner: () -> Unit,
    addService: () -> Unit,
    errorMessage: String?,
){
    CustomStatusBar()
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues()
                    .calculateTopPadding()
            ),
        topBar = {
            CustomAppBar(
                text = title,
                icon = painterResource(R.drawable.ic_arrow_back),
                iconText = backTitle,
                onClick = onBack
            )
        }
    )
    { paddingValues ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_body)),

            ) {
            Text(
                text = stringResource(R.string.service_editor_desc),
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))


            CustomTextField(
                value = state.serviceName,
                onValueChange = updateServiceName,
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.service_field_title_add)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

            CustomTextField(
                value = state.secretKey,
                onValueChange = updateSecretKey,
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.service_field_title_key)
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

            Text(
                text = stringResource(R.string.service_text_or),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.LocalColors.link.text
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

            CustomButton(
                modifier = Modifier.wrapContentWidth(),
                onClick = onOpenScanner,
                style = ButtonStyle.Secondary,
                text = stringResource(R.string.service_scan_btn),
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            CustomLoadingButton(
                modifier = Modifier.width(160.dp),
                onClick = addService,
                style = ButtonStyle.Primary,
                text = stringResource(R.string.service_save_btn)
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddServiceContentPreview(){
    AppTheme {
        FormContent(
            state = AddServiceState(
                serviceName = "GitHub",
                secretKey = "JBSWY3DPEHPK3PXP",
                errorMessage = "Некорректный формат ключа Base32"
            ),
            title = "Добавить сервис",
            backTitle = "Назад",
            onBack = {},
            updateServiceName = {},
            updateSecretKey = {},
            onOpenScanner = {},
            addService = {},
            errorMessage = null
        )
    }
}
