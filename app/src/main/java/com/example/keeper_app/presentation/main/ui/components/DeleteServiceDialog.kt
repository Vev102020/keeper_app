package com.example.keeper_app.presentation.main.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.keeper_app.R
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.custom.CustomAlertDialog

private object DeleteDialogStrings{
    val title = R.string.delete_dialog_title
    val text = R.string.delete_dialog_text
    val confirmText = R.string.delete_dialog_confirm
    val dismissText = R.string.delete_dialog_dismiss
}

@Composable
fun DeleteServiceDialog(
    service: ServiceDb? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,

){
    CustomAlertDialog(
        title = stringResource(DeleteDialogStrings.title),
        text = stringResource(DeleteDialogStrings.text),
        onConfirm = onConfirm,
        confirmTitle = stringResource(DeleteDialogStrings.confirmText),
        onDismiss = onDismiss,
        dismissTitle = stringResource(DeleteDialogStrings.dismissText),
        service = service,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeleteServiceDialogPreview(){
    AppTheme {
        DeleteServiceDialog(
            service = null,
            onConfirm = {},
            onDismiss = {},
        )
    }
}