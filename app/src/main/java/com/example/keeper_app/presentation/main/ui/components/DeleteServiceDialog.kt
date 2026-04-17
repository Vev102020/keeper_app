package com.example.keeper_app.presentation.main.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.presentation.ui.theme.custom.ButtonStyle
import com.example.keeper_app.presentation.ui.theme.custom.CustomButton

@Composable
fun DeleteServiceDialog(
    service: ServiceDb? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Удалить ${service?.name}?",
    text: String = "Данные будут навсегда удалены.",
    confirmTitle: String = "Отмена",
    dismissTitle: String = "Удалить",
){
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Text(text)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            CustomButton(
                onClick = onConfirm,
                text = confirmTitle,
                style = ButtonStyle.Secondary
            )
        },
        dismissButton = {
            CustomButton(
                onClick = onDismiss,
                text = dismissTitle,
                style = ButtonStyle.Primary
            )
        }
    )
}