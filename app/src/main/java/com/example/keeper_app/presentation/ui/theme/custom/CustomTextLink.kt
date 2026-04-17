package com.example.keeper_app.presentation.ui.theme.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TextLink(
    text: String,
    onClick: () -> Unit,
    textColor: Color,
    style: TextStyle,
    fontWeight: FontWeight? = null
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = textColor,
            style = style,
            fontWeight = fontWeight?: FontWeight.Normal)
    }
}