package com.example.keeper_app.presentation.main.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.keeper_app.R
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.presentation.ui.theme.custom.ButtonStyle
import com.example.keeper_app.presentation.ui.theme.custom.CustomButton

@Composable
fun DetailServiceDialog(
    service: ServiceDb?,
    onDismiss: () -> Unit,
    title: String = "Totp",
    dismissTitle: String = "Закрыть",
    timer: String = "0",
){
    val totp: String = service?.name ?: "000000"

    Dialog(
        onDismissRequest = {onDismiss()}
    ) {
        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indent_text)))
            Text(
                text = service?.name ?: "Название",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.indent_medium_super)))
        Row {
            Box(
                modifier = Modifier
                .size(width = 56.dp, height = 56.dp)
                .background(MaterialTheme.colorScheme.outline)
            ){
                Text(timer)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ){
                Text(totp)
            }
            Box(
                modifier = Modifier
                    .size(width = 56.dp, height = 56.dp)
                    .background(MaterialTheme.colorScheme.outline)
            ){
                Icon(
                    painter = painterResource(R.drawable.ic_copy),
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                )
            }
        }
        CustomButton(
            onClick = onDismiss,
            text = dismissTitle,
            style = ButtonStyle.Primary
        )
    }
}