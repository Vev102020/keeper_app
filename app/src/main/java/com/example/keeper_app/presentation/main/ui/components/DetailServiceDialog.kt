package com.example.keeper_app.presentation.main.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keeper_app.R
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors
import com.example.keeper_app.presentation.ui.theme.custom.TextLink
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.keeper_app.data.storage.entities.Totp
import com.example.keeper_app.presentation.main.viewmodel.DetailServiceViewModel
import com.example.keeper_app.presentation.main.viewmodel.TotpUiState

private object DetailDialogStrings{
    val title = R.string.detail_dialog_title
    val name = R.string.detail_dialog_name
    val key = R.string.detail_dialog_key
    val timer = R.string.detail_dialog_timer
    val copy = R.string.detail_dialog_copy
    val dismissTitle = R.string.detail_dialog_dismiss
}



@Composable
fun DetailServiceDialog(
    service: ServiceDb?,
    onDismiss: () -> Unit,
    detailViewModel: DetailServiceViewModel = hiltViewModel(),
){
    val uiState by detailViewModel.uiState.collectAsState()
    var dataTotp by remember { mutableStateOf<Totp?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Загружаем и расшифровываем totp
    LaunchedEffect(service?.id) {
        if (service?.totp == null) {
            dataTotp = null
            isLoading = false
            return@LaunchedEffect
        }

        try {
            dataTotp = detailViewModel.decryptTotp(service.totp)
        } catch (e: Exception) {
            Log.e("DetailServiceDialog", "Не удалось расшифровать Totp", e)
        } finally {
            isLoading = false
        }
        Log.i("dataTotp = ", "$dataTotp")
    }

    // Запуск генерации
    LaunchedEffect(dataTotp) {
        if (dataTotp != null) {
            detailViewModel.startTotpGeneration(dataTotp!!)
        } else {
            detailViewModel.stop()
        }
    }

    // Очистка при закрытии
    DisposableEffect(Unit) {
        onDispose {
            detailViewModel.stop()
        }
    }

    DialogContent(
        service = service,
        uiState = uiState,
        dataTotp = dataTotp,
        isLoading = isLoading,
        onDismiss = {onDismiss()},
    )

}

@Composable
private fun DialogContent(
    service: ServiceDb?,
    uiState: TotpUiState,
    dataTotp: Totp?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
){
    val context = LocalContext.current
    // Android-менеджер буфера обмена
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val heightTotpContent = 56.dp

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.LocalColors.dialog.background,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
        ) {
            Column (
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_dialog))
            ){
                Row {
                    Text(
                        text = stringResource(DetailDialogStrings.title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))
                    Text(
                        text = service?.name ?: stringResource(DetailDialogStrings.name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))

                // Контент
                Box(
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.LocalColors.totpDialog.border, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                ){
                    Row {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(width = heightTotpContent, height = heightTotpContent)
                                .background(MaterialTheme.LocalColors.totpDialog.iconBackground),
                        ){
                            Column (
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(-4.dp)
                            ){
                                Text(
                                    text = when {
                                        isLoading -> "•••"
                                        dataTotp == null -> stringResource(DetailDialogStrings.timer)
                                        uiState is TotpUiState.Success -> "${uiState.timerValue}"
                                        else -> "--"
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.LocalColors.totpDialog.timer,
                                    modifier = Modifier.padding(0.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier
                            .width(1.dp)
                            .height(heightTotpContent)
                            .background(MaterialTheme.LocalColors.totpDialog.border)
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                        ){
                            Text(
                                text = when {
                                    isLoading -> "••••••"
                                    dataTotp == null -> stringResource(DetailDialogStrings.key)
                                    uiState is TotpUiState.Success -> uiState.code
                                    else -> "------"
                                },
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.LocalColors.totpDialog.totp,
                                letterSpacing = 2.sp
                            )
                        }
                        Spacer(modifier = Modifier
                            .width(1.dp)
                            .height(heightTotpContent)
                            .background(MaterialTheme.LocalColors.totpDialog.border)
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(width = heightTotpContent, height = heightTotpContent)
                                .background(MaterialTheme.LocalColors.totpDialog.iconBackground)
                                .clickable{
                                    when(val state = uiState){
                                        is TotpUiState.Success -> {
                                            clipboard.setPrimaryClip(
                                                ClipData.newPlainText("Totp code", state.code)
                                            )
                                            Toast.makeText(
                                                context,
                                                "Скопировано",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        else -> {
                                            Toast.makeText(
                                                context,
                                                "Не удалось скопировать",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                }
                        ){
                            Icon(
                                painter = painterResource(R.drawable.ic_copy),
                                contentDescription = stringResource(DetailDialogStrings.copy),
                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium)),
                                tint = MaterialTheme.LocalColors.totpDialog.icon
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    TextLink(
                        text = stringResource(DetailDialogStrings.dismissTitle),
                        textColor = MaterialTheme.LocalColors.dialog.dismiss,
                        style = MaterialTheme.typography.bodyLarge,
                        onClick = onDismiss,
                        fontWeight = FontWeight.Medium,
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailServiceDialogPreview(){
    AppTheme {
        DialogContent(
            service = null,
            uiState = TotpUiState.Idle,
            isLoading = false,
            dataTotp = null,
            onDismiss = {},
        )
    }
}