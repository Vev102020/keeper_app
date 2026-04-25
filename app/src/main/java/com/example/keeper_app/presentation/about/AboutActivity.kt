package com.example.keeper_app.presentation.about

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.keeper_app.presentation.about.ui.screen.AboutScreen
import com.example.keeper_app.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutActivity : ComponentActivity() {
    private companion object{
        const val TAG = "AboutActivity"
    }

    private lateinit var version : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        version = getAppVersion()

        setContent {
            AppTheme {
                AboutScreen(
                    version = version,
                    onBack = {finish()}
                )
            }
        }
    }

    fun getAppVersion(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                0
            )
            packageInfo.versionName ?: "Не указана"
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка получения версии", e)
            "Не указана"
        }
    }
}