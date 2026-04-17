package com.example.keeper_app.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.keeper_app.presentation.ui.theme.CustomButtonColors

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF191919)
val Blue = Color(0xFF0095FF)
val LightBlue = Color(0xFF38ACFF)
val Grey = Color(0xFFBABECC)
val LightGrey = Color(0xFFD7DAE5)
val LightGreyUltra = Color(0xFFF0F0F0)
val Red = Color(0xFFE8316C)
val Transparent = Color(0x00000000)

data class CustomThemeColors(
    var primary: Color,
    var onPrimary: Color,
    var primaryContainer: Color,
    var onPrimaryContainer: Color,
    var secondary: Color,
    var onSecondary: Color,
    var secondaryContainer: Color,
    var onSecondaryContainer: Color,
    var tertiary: Color,
    var onTertiary: Color,
    var tertiaryContainer: Color,
    var onTertiaryContainer: Color,
    var error: Color,
    var errorContainer: Color,
    var onError: Color,
    var onErrorContainer: Color,
    var background: Color,
    var onBackground: Color,
    var surface: Color,
    var onSurface: Color,
    var surfaceVariant: Color,
    var onSurfaceVariant: Color,
    var outline: Color,
    var inverseOnSurface: Color,
    var inverseSurface: Color,
    var inversePrimary: Color,
    var surfaceTint: Color,
    var outlineVariant: Color,
    var scrim: Color,
)

object ThemeColors{
    val light = CustomThemeColors(
     primary = Blue,
     onPrimary = White,
     primaryContainer = LightBlue,
     onPrimaryContainer = White,
     secondary = Transparent,
     onSecondary = Black,
     secondaryContainer = LightGrey,
     onSecondaryContainer = Black,
     tertiary = Grey,
     onTertiary = Grey,
     tertiaryContainer = Color(0xFFC1E8FB),
     onTertiaryContainer = Color(0xFF001F29),
     error = Red,
     errorContainer = Red,
     onError = White,
     onErrorContainer = White,
     background = White,
     onBackground = Black,
     surface = White,
     onSurface = Black,
     surfaceVariant = LightGrey,
     onSurfaceVariant = Grey,
     outline = LightGrey,
     inverseOnSurface = Black,
     inverseSurface = White,
     inversePrimary = Blue,
     surfaceTint = LightBlue,
     outlineVariant = Color(0xFF808C85),
     scrim = Color(0x33000000),
    )
    val dark = CustomThemeColors(
        primary = Blue,
        onPrimary = White,
        primaryContainer = LightBlue,
        onPrimaryContainer = White,
        secondary = Transparent,
        onSecondary = Black,
        secondaryContainer = LightGrey,
        onSecondaryContainer = Black,
        tertiary = Grey,
        onTertiary = Grey,
        tertiaryContainer = Color(0xFFC1E8FB),
        onTertiaryContainer = Color(0xFF001F29),
        error = Red,
        errorContainer = Red,
        onError = White,
        onErrorContainer = White,
        background = Black,
        onBackground = White,
        surface = Black,
        onSurface = White,
        surfaceVariant = Grey,
        onSurfaceVariant = LightGrey,
        outline = LightGrey,
        inverseOnSurface = Black,
        inverseSurface = White,
        inversePrimary = Blue,
        surfaceTint = LightBlue,
        outlineVariant = Color(0xFF808C85),
        scrim = Color(0x33000000),
    )
}

/* Статус бар */
sealed class StatusBarTheme(
    val backgroundColor: Color,
    val isLightIcons: Boolean
) {
    object Dark : StatusBarTheme(backgroundColor = White, isLightIcons = true)
    object Light : StatusBarTheme(backgroundColor = Black, isLightIcons = false)
}
/* Расширение Material */

data class LocalColors(
    val primaryButton: CustomButtonColors,
    val secondaryButton: CustomButtonColors,
    val link: CustomLinkColors,
    val textField: CustomTextFieldColors,
    val appBar: CustomAppBarColors,
    val drawer: CustomDrawerColors,
)

data class CustomAppBarColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val navigationIconContentColor: Color,
    val actionIconContentColor: Color,
    val shape: Color
)
data class CustomDrawerColors(
    val background: Color,
    val text: Color,
    val shape: Color,
    val  selectedContainerColor: Color,
    val  unselectedContainerColor: Color,
    val  selectedIconColor: Color,
    val  unselectedIconColor: Color,
    val  selectedTextColor: Color,
    val  unselectedTextColor: Color,
    val  selectedBadgeColor: Color,
    val  unselectedBadgeColor: Color
)

//Кнопки
data class CustomButtonColors(
    val background: Color,
    val disabledBackground: Color,
    val text: Color,
    val disabledText: Color,
    val shape:Color,
    val disabledShape: Color,
)
data class CustomLinkColors(
    val text: Color,
)
//Поля
data class CustomTextFieldColors(
    val focusedTextColor: Color,
    val unfocusedTextColor: Color,
    val disabledTextColor: Color,
    val errorTextColor: Color,
    // Фон
    val focusedContainerColor: Color,
    val unfocusedContainerColor: Color,
    val disabledContainerColor: Color,
    val errorContainerColor: Color,
    // Курсор
    val cursorColor: Color,
    val errorCursorColor: Color,
    // Бордер (индикатор)
    val focusedIndicatorColor: Color,
    val unfocusedIndicatorColor: Color,
    val disabledIndicatorColor: Color,
    val errorIndicatorColor: Color,
    // Иконки
    val focusedLeadingIconColor: Color,
    val unfocusedLeadingIconColor: Color,
    val disabledLeadingIconColor: Color,
    val errorLeadingIconColor: Color,
    val focusedTrailingIconColor: Color,
    val unfocusedTrailingIconColor: Color,
    val disabledTrailingIconColor: Color,
    val errorTrailingIconColor: Color,
    // Лейблы
    val focusedLabelColor: Color,
    val unfocusedLabelColor: Color,
    val disabledLabelColor: Color,
    val errorLabelColor: Color,
    // Плейсхолдеры
    val focusedPlaceholderColor: Color,
    val unfocusedPlaceholderColor: Color,
    val disabledPlaceholderColor: Color,
    val errorPlaceholderColor: Color,
)

object LightColors{
    val primaryButton = CustomButtonColors(
        background = ThemeColors.light.primary,
        disabledBackground = Grey,
        text = ThemeColors.light.onPrimary,
        disabledText = White,
        shape = ThemeColors.light.primary,
        disabledShape = Grey,
    )
    val secondaryButton = CustomButtonColors(
        background = Transparent,
        disabledBackground = Transparent,
        text = Black,
        disabledText = Grey,
        shape = LightColorScheme.outline,
        disabledShape = Grey,
    )

    val link = CustomLinkColors(
        text = Grey
    )

    val textField = CustomTextFieldColors(
        focusedTextColor = Black,
        unfocusedTextColor = Black,
        disabledTextColor = White,
        errorTextColor = Red,
        // Фон
        focusedContainerColor = Transparent,
        unfocusedContainerColor = Transparent,
        disabledContainerColor = Grey,
        errorContainerColor = Transparent,
        // Курсор
        cursorColor = LightColorScheme.primary,
        errorCursorColor = LightColorScheme.error,
        // Бордер (индикатор)
        focusedIndicatorColor = LightColorScheme.primary,
        unfocusedIndicatorColor = LightColorScheme.outline,
        disabledIndicatorColor = Grey,
        errorIndicatorColor= LightColorScheme.error,
        // Иконки
        focusedLeadingIconColor = LightColorScheme.primary,
        unfocusedLeadingIconColor = Grey,
        disabledLeadingIconColor = White,
        errorLeadingIconColor = Red,
        focusedTrailingIconColor = LightColorScheme.primary,
        unfocusedTrailingIconColor = Grey,
        disabledTrailingIconColor = White,
        errorTrailingIconColor = Red,
        // Лейблы
        focusedLabelColor = LightColorScheme.primary,
        unfocusedLabelColor = Grey,
        disabledLabelColor = Grey,
        errorLabelColor = Red,
        // Плейсхолдеры
        focusedPlaceholderColor = Grey,
        unfocusedPlaceholderColor = Grey,
        disabledPlaceholderColor = Grey,
        errorPlaceholderColor = Grey,
    )

    val appBar = CustomAppBarColors(
        containerColor = Transparent,
        titleContentColor = Black,
        navigationIconContentColor = Black,
        actionIconContentColor = Black,
        shape = LightGreyUltra,
    )

    val drawer = CustomDrawerColors(
        background = Black,
        text = White,
        shape = LightGrey,
        selectedContainerColor = Transparent,
        unselectedContainerColor= Transparent,
        selectedIconColor= ThemeColors.light.primary,
        unselectedIconColor= Black,
        selectedTextColor= ThemeColors.light.primary,
        unselectedTextColor= Black,
        selectedBadgeColor= ThemeColors.light.primary,
        unselectedBadgeColor= Black,
    )
    val LocalColors = LocalColors(
        primaryButton,
        secondaryButton,
        link,
        textField,
        appBar,
        drawer)
}

object DarkColors {
    val primaryButton = CustomButtonColors(
        background = ThemeColors.dark.primary,
        disabledBackground = Grey,
        text = White,
        disabledText = White,
        shape = ThemeColors.dark.primary,
        disabledShape = Grey,
    )
    val secondaryButton = CustomButtonColors(
        background = Transparent,
        disabledBackground = Transparent,
        text = Black,
        disabledText = Grey,
        shape = LightColorScheme.outline,
        disabledShape = Grey,
    )
    val link = CustomLinkColors(
        text = Grey
    )

    val textField = CustomTextFieldColors(
        focusedTextColor = Black,
        unfocusedTextColor = Black,
        disabledTextColor = White,
        errorTextColor = Red,
        // Фон
        focusedContainerColor = Transparent,
        unfocusedContainerColor = Transparent,
        disabledContainerColor = Grey,
        errorContainerColor = Transparent,
        // Курсор
        cursorColor = LightColorScheme.primary,
        errorCursorColor = LightColorScheme.error,
        // Бордер (индикатор)
        focusedIndicatorColor = LightColorScheme.primary,
        unfocusedIndicatorColor = LightColorScheme.outline,
        disabledIndicatorColor = Grey,
        errorIndicatorColor= LightColorScheme.error,
        // Иконки
        focusedLeadingIconColor = LightColorScheme.primary,
        unfocusedLeadingIconColor = Grey,
        disabledLeadingIconColor = White,
        errorLeadingIconColor = Red,
        focusedTrailingIconColor = LightColorScheme.primary,
        unfocusedTrailingIconColor = Grey,
        disabledTrailingIconColor = White,
        errorTrailingIconColor = Red,
        // Лейблы
        focusedLabelColor = LightColorScheme.primary,
        unfocusedLabelColor = Grey,
        disabledLabelColor = Grey,
        errorLabelColor = Red,
        // Плейсхолдеры
        focusedPlaceholderColor = Grey,
        unfocusedPlaceholderColor = Grey,
        disabledPlaceholderColor = Grey,
        errorPlaceholderColor = Grey,
    )

    val appBar = CustomAppBarColors(
        containerColor = Transparent,
        titleContentColor = Black,
        navigationIconContentColor = Black,
        actionIconContentColor = Black,
        shape = LightGreyUltra,
    )

    val drawer = CustomDrawerColors(
        background = White,
        text = Black,
        shape = LightGrey,
        selectedContainerColor = Transparent,
        unselectedContainerColor= Transparent,
        selectedIconColor= ThemeColors.light.primary,
        unselectedIconColor= Black,
        selectedTextColor= ThemeColors.light.primary,
        unselectedTextColor= Black,
        selectedBadgeColor= ThemeColors.light.primary,
        unselectedBadgeColor= Black,
    )
    val LocalColors = LocalColors(
        primaryButton,
        secondaryButton,
        link,
        textField,
        appBar,
        drawer)
}

val LocalExtendedColors = staticCompositionLocalOf<LocalColors> {
    LightColors.LocalColors
}


