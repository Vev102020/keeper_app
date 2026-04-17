package com.example.keeper_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.keeper_app.presentation.auth.AuthActivity
import com.example.keeper_app.presentation.main.MainActivity
import com.example.keeper_app.presentation.pin.PinActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppActivity : ComponentActivity() {
    private companion object{
        const val TAG = "AppActivity"
    }
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.appState
                    .collect { state ->
                    Log.i(TAG, "🎯 AppActivity: получено новое состояние: $state")
                    val intent = when (state) {
                        is AppState.Unauthenticated -> {
                            Log.i(TAG, "Unauthenticated")
                            Intent(this@AppActivity, AuthActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        }

                        is AppState.Authenticated -> {
                            Log.i(TAG, "Authenticated")
                            Intent(this@AppActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
//                            if (state.requiresPin) {
//                                Intent(this@AppActivity, PinActivity::class.java)
//                            } else {
//                                Intent(this@AppActivity, MainActivity::class.java)
//                            }
                        }

                        is AppState.PinRequired -> {
                            Log.i(TAG, "PinRequired")
                            Intent(this@AppActivity, PinActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        }

                        is AppState.Logout, is AppState.ForceLogout -> {
                            Log.i(TAG, "Logout")
                            Intent(this@AppActivity, AuthActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        }

                        else -> {
                            Log.i(TAG, "ELSE")
                            Intent(this@AppActivity, AuthActivity::class.java)
                        }
                    }
                    Log.i(TAG, "📲 Выполняем startActivity и finish()")

                    startActivity(intent)

                }
            }
        }

    }

}