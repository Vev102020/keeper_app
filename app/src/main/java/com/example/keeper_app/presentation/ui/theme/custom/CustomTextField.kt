package com.example.keeper_app.presentation.ui.theme.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keeper_app.presentation.ui.theme.LocalColors

@Composable
private fun textFieldStyle(): TextFieldColors {
    val style = MaterialTheme.LocalColors.textField
    return TextFieldDefaults.colors(
            // Текст
            focusedTextColor = style.focusedTextColor,
            unfocusedTextColor = style.unfocusedTextColor,
            disabledTextColor = style.disabledTextColor,
            errorTextColor = style.errorTextColor,

            // Фон
            focusedContainerColor = style.focusedContainerColor,
            unfocusedContainerColor = style.unfocusedContainerColor,
            disabledContainerColor = style.disabledContainerColor,
            errorContainerColor = style.errorContainerColor,

            // Курсор
            cursorColor = style.cursorColor,
            errorCursorColor = style.errorCursorColor,

            // Бордер (индикатор)
            focusedIndicatorColor = style.focusedIndicatorColor,
            unfocusedIndicatorColor = style.unfocusedIndicatorColor,
            disabledIndicatorColor = style.disabledIndicatorColor,
            errorIndicatorColor = style.errorIndicatorColor,

            // Иконки
            focusedLeadingIconColor = style.focusedLeadingIconColor,
            unfocusedLeadingIconColor = style.unfocusedLeadingIconColor,
            disabledLeadingIconColor = style.disabledLeadingIconColor,
            errorLeadingIconColor = style.errorLeadingIconColor,
            focusedTrailingIconColor = style.focusedTrailingIconColor,
            unfocusedTrailingIconColor = style.unfocusedTrailingIconColor,
            disabledTrailingIconColor = style.disabledTrailingIconColor,
            errorTrailingIconColor = style.errorTrailingIconColor,

            // Лейблы
            focusedLabelColor = style.focusedLabelColor,
            unfocusedLabelColor = style.unfocusedLabelColor,
            disabledLabelColor = style.disabledLabelColor,
            errorLabelColor = style.errorLabelColor,

            // Плейсхолдеры
            focusedPlaceholderColor = style.focusedPlaceholderColor,
            unfocusedPlaceholderColor = style.unfocusedPlaceholderColor,
            disabledPlaceholderColor = style.disabledPlaceholderColor,
            errorPlaceholderColor = style.errorPlaceholderColor
    )
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val hasLabel = label != null
    val minHeight = if (hasLabel) 64.dp else 50.dp

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        minLines = if (hasLabel) 2 else 1,
        shape = MaterialTheme.shapes.medium,
        colors =  textFieldStyle(),
        label = if (label != null) { { Text(label, fontWeight = FontWeight.Medium) } } else null,
        placeholder = if (placeholder != null) { { Text(placeholder) } } else null,
        textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .requiredHeightIn(max = minHeight),
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomTextFieldPreview(){
    val text = remember { mutableStateOf("test123") }

    CustomTextField(
        value = text.value,
        onValueChange = {text.value = it},
        label = "Заголовок",
        placeholder = "Заголовок"
    )
}