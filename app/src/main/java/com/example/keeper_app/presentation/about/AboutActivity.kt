package com.example.keeper_app.presentation.about

import android.content.Context
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

    val version : String = getAppVersion(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "$TAG Запущена")
        setContent {
            AppTheme {
                AboutScreen(
                    version = version,
                    onBack = {finish()}
                )
            }
        }
    }

    fun getAppVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                0
            )
            packageInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }
}