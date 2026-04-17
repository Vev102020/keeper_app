package com.example.keeper_app.presentation.ui.theme.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keeper_app.R
import com.example.keeper_app.presentation.ui.theme.AppTheme
import com.example.keeper_app.presentation.ui.theme.LocalColors



@Composable
private fun primaryButtonStyle(): ButtonColors {
    val style = MaterialTheme.LocalColors.primaryButton
    return ButtonDefaults.buttonColors(
        containerColor = style.background,
        contentColor = style.text,
        disabledContainerColor =  style.background.copy(alpha = 0.8f),
        disabledContentColor = style.text.copy(alpha = 0.8f)
    )
}

@Composable
private fun secondaryButtonStyle(): ButtonColors {
    val style = MaterialTheme.LocalColors.secondaryButton
    return ButtonDefaults.buttonColors(
        containerColor = style.background,
        contentColor = style.text,
        disabledContainerColor =  style.background,
        disabledContentColor = style.text.copy(alpha = 0.8f)
    )
}

@Composable
private fun primaryButtonBorder(enabled: Boolean): BorderStroke {
    val style = MaterialTheme.LocalColors.primaryButton
    return BorderStroke(1.dp, if(enabled) style.shape else style.shape.copy(alpha = 0.0f))
}

@Composable
private fun secondaryButtonBorder(enabled: Boolean): BorderStroke {
    val style = MaterialTheme.LocalColors.secondaryButton
    return  BorderStroke(1.dp, if(enabled) style.shape else style.shape)
}

@Composable
private fun contentPadding(): PaddingValues {
    return PaddingValues(horizontal = 20.dp, vertical = 0.dp)
}
@Composable
private fun primaryProgressStyle(): Color {
    val style = MaterialTheme.LocalColors.primaryButton
    return style.text
}

@Composable
private fun secondaryProgressStyle(): Color {
    val style = MaterialTheme.LocalColors.secondaryButton
    return style.text
}


@Composable
fun CustomLoadingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    enabled: Boolean = !isLoading,
    style: ButtonStyle = ButtonStyle.Primary,
    text: String,
){
    val buttonColors = when (style) {
        ButtonStyle.Primary -> primaryButtonStyle()
        ButtonStyle.Secondary -> secondaryButtonStyle()
    }

    val border = when (style) {
        ButtonStyle.Primary -> primaryButtonBorder(enabled)
        ButtonStyle.Secondary -> secondaryButtonBorder(enabled)
    }

    val circularProgressIndicatorColor = when (style) {
        ButtonStyle.Primary -> primaryProgressStyle()
        ButtonStyle.Secondary -> secondaryProgressStyle()
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
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium)),
                strokeWidth = 2.dp,
                color = circularProgressIndicatorColor
            )
        }else{
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
private fun ProvideCustomLoadingButton(){
    var buttonState by remember { mutableStateOf(false) }

    AppTheme {
        CustomLoadingButton(
            modifier = Modifier.width(160.dp),
            onClick = {},
            isLoading = buttonState,
            style = ButtonStyle.Primary,
            text = "Отправить",
        )
    }
}