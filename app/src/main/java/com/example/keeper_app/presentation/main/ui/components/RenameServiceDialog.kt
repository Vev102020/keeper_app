package com.example.keeper_app.presentation.main.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.keeper_app.R
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.custom.CustomAlertDialog
import com.example.keeper_app.presentation.ui.theme.custom.CustomTextField


private object RenameDialogStrings{
    val title = R.string.rename_dialog_title
    val inputLabel = R.string.rename_dialog_input_label
    val confirmText = R.string.rename_dialog_confirm
    val dismissText = R.string.rename_dialog_dismiss
}
@Composable
fun RenameServiceDialog(
    service: ServiceDb? = null,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    message: String? = null
){
    var newName by remember (service) { mutableStateOf(service?.name ?: "") }

    CustomAlertDialog(
        title = stringResource(RenameDialogStrings.title),
        onConfirm = { onConfirm(newName) },
        confirmTitle = stringResource(RenameDialogStrings.confirmText),
        onDismiss = onDismiss,
        dismissTitle = stringResource(RenameDialogStrings.dismissText),
        message = message,
        content = {
            CustomTextField(
                value = newName,
                onValueChange = { newName = it },
                placeholder = stringResource(RenameDialogStrings.inputLabel),
                modifier = Modifier.fillMaxWidth()
            )
        }
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RenameServiceDialogPreview(){
    AppTheme {
        RenameServiceDialog(
            service = null,
            onConfirm = {},
            onDismiss = {},
        )
    }
}