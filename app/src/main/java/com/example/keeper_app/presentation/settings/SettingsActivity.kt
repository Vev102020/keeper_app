package com.example.keeper_app.presentation.settings

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.keeper_app.presentation.settings.ui.screen.SettingsScreen
import com.example.keeper_app.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity() : ComponentActivity() {
    private companion object{
        const val TAG = "SettingsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "$TAG Запущена")
        setContent {
            AppTheme {
                SettingsScreen(
                    onBack = {finish()}
                )
            }
        }
    }
}