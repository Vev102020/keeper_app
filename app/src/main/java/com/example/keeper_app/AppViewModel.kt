package com.example.keeper_app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keeper_app.data.network.repo.AuthRepository
import com.example.keeper_app.data.network.repo.PinRepository
import com.example.keeper_app.presentation.auth.viewmodel.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val pinRepository: PinRepository,
) : ViewModel() {
    private companion object{
        const val TAG = "AppViewModel"
    }
    private val _appState = MutableStateFlow<AppState>(AppState.Unauthenticated)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    init {

        viewModelScope.launch {
            authRepository.checkAuthState()
        }

        Log.i(TAG, "🔄 AppViewModel: начинаем слушать authState")
        authRepository.authState.onEach { authState ->
            Log.i(TAG,"$authState" )
            when (authState){
                is AuthState.Success -> {
                    Log.i(TAG, "✅ Устанавливаем appState = Authenticated")
                    _appState.value = AppState.Authenticated(
                        userId = authState.user.firebaseId,
                        requiresPin = false
                    )

                }
                is AuthState.Idle -> {
                    Log.i(TAG, "🔓 Устанавливаем appState = Unauthenticated")
                    _appState.value = AppState.Unauthenticated
                }
                is AuthState.Error -> {
                    Log.e(TAG, "❌ Ошибка: ${authState.message}")
                    _appState.value = AppState.Unauthenticated
                }
                is AuthState.Loading -> {
                    //Экран загрузки
                }
            }

        }.launchIn(viewModelScope)

    }

    fun logout(){
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun navigateToAuth(){
        authRepository.setAuthState(AuthState.Idle)
    }
}