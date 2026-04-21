package com.example.keeper_app.presentation.ui.theme.custom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.keeper_app.R
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors

@Composable
fun CustomAlertDialog(
    title: String,
    text: String? = null,
    onConfirm: () -> Unit,
    confirmTitle: String,
    onDismiss: () -> Unit,
    dismissTitle: String,
    service: ServiceDb? = null,
    message: String? = null,
    content: @Composable (() -> Unit)? = null
){
    AlertDialog(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_dialog))
            .fillMaxWidth()
            .widthIn(max = 320.dp)
            .padding(dimensionResource(R.dimen.padding_dialog))
        ,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.LocalColors.dialog.background,
        titleContentColor = MaterialTheme.LocalColors.dialog.title,
        textContentColor = MaterialTheme.LocalColors.dialog.text,

        title = {
                if(service == null){
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }else{
                    Row {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))
                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
        },
        text = {
            if(text != null){
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if(content != null){
                content()
            }


            if(message != null){
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.LocalColors.dialog.message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_small)))
            TextLink(
                text = confirmTitle,
                textColor = MaterialTheme.LocalColors.dialog.confirm,
                style = MaterialTheme.typography.bodyLarge,
                onClick = onConfirm,
                fontWeight = FontWeight.Medium,
            )
        },
        dismissButton = {
            TextLink(
                text = dismissTitle,
                textColor = MaterialTheme.LocalColors.dialog.dismiss,
                style = MaterialTheme.typography.bodyLarge,
                onClick = onDismiss,
                fontWeight = FontWeight.Medium,
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomAlertDialogPreview(){
    AppTheme {
        CustomAlertDialog(
            title = "Удалить сервис?",
            text = "Данные после удаления будут удалены безвозвратно",
            onConfirm = {},
            confirmTitle = "Удалить",
            onDismiss = {},
            dismissTitle = "Отмена",
            service = null,
            message = null,
        )
    }

}