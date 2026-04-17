package com.example.keeper_app.presentation.pin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.keeper_app.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinActivity : ComponentActivity() {
    private companion object{
        const val TAG = "PinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "$TAG Open")
        setContent {
            AppTheme {

            }
        }
    }
}