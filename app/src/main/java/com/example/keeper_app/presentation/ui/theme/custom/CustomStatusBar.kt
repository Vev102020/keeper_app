package com.example.keeper_app.presentation.ui.theme.custom

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.keeper_app.presentation.ui.theme.StatusBarTheme

@Composable
fun CustomStatusBar() {
    val context = LocalContext.current
    val window = (context as? Activity)?.window
    if (window == null) return
    // Определяем, включена ли тёмная тема в системе
    val isDarkTheme = isSystemInDarkTheme()

    val currentTheme = if (isDarkTheme) StatusBarTheme.Dark else StatusBarTheme.Light

    DisposableEffect(isDarkTheme) {
        // edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val insetsController = WindowInsetsControllerCompat(window, window.decorView)

        insetsController.isAppearanceLightStatusBars = currentTheme.isLightIcons
        insetsController.isAppearanceLightNavigationBars = currentTheme.isLightIcons

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .background(currentTheme.backgroundColor)
    )
}