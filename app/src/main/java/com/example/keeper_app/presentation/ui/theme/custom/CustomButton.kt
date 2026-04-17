package com.example.keeper_app.presentation.ui.theme.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors


enum class ButtonStyle {
    Primary,
    Secondary
}

@Composable
private fun primaryButtonStyle(): ButtonColors {
    val style = MaterialTheme.LocalColors.primaryButton
    return ButtonDefaults.buttonColors(
        containerColor = style.background,
        contentColor = style.text,
        disabledContainerColor =  style.disabledBackground,
        disabledContentColor = style.disabledText
    )
}

@Composable
private fun secondaryButtonStyle(): ButtonColors {
    val style = MaterialTheme.LocalColors.secondaryButton
    return ButtonDefaults.buttonColors(
        containerColor = style.background,
        contentColor = style.text,
        disabledContainerColor =  style.disabledBackground,
        disabledContentColor = style.disabledText
    )
}

@Composable
private fun primaryButtonBorder(enabled: Boolean): BorderStroke {
    val style = MaterialTheme.LocalColors.primaryButton
    return BorderStroke(1.dp, if(enabled) style.shape else style.disabledShape)
}

@Composable
private fun secondaryButtonBorder(enabled: Boolean): BorderStroke {
    val style = MaterialTheme.LocalColors.secondaryButton
    return  BorderStroke(1.dp, if(enabled) style.shape else style.disabledShape)
}

@Composable
private fun contentPadding(): PaddingValues {
    return PaddingValues(horizontal = 20.dp, vertical = 0.dp)
}
@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    style: ButtonStyle = ButtonStyle.Primary,
    text: String? = null,
    icon: Painter? = null,
){


    val buttonColors = when (style) {
        ButtonStyle.Primary -> primaryButtonStyle()
        ButtonStyle.Secondary -> secondaryButtonStyle()
    }

    val border = when (style) {
        ButtonStyle.Primary -> primaryButtonBorder(enabled)
        ButtonStyle.Secondary -> secondaryButtonBorder(enabled)
    }

    Button(
        modifier = modifier.height(46.dp),
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = buttonColors,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        border = border,
        contentPadding = contentPadding(),
    ) {
        if(icon != null){
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
            )
        }
        if(icon != null && text != null){
            Spacer(Modifier.width(dimensionResource(R.dimen.indent_text)))
        }
        if(text != null){
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ProvideCustomButton(){
    AppTheme {
        CustomButton(
            modifier = Modifier.width(160.dp),
            onClick = {},
//            enabled = false,
            style = ButtonStyle.Primary,
            text = "Отправить",
//            icon = painterResource(id = R.drawable.ic_visibility)
        )
    }
}