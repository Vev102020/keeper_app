package com.example.keeper_app.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keeper_app.data.storage.entities.Totp
import com.example.keeper_app.domain.usecases.service.GenerateTotpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TotpUiState {
    data object Idle : TotpUiState
    data class Success(val code: String, val timerValue: Int) : TotpUiState
    data class Error(val message: String) : TotpUiState
}

@HiltViewModel
class TotpViewModel @Inject constructor(
    private val generateTotpUseCase: GenerateTotpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TotpUiState>(TotpUiState.Idle)
    val uiState: StateFlow<TotpUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startTotpGeneration(totp: Totp){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive){
                try {
                    val code = generateTotpUseCase.generateTotp(totp)
                    val currentTime = System.currentTimeMillis() / 1000
                    val timerValue = (totp.period - (currentTime % totp.period)).toInt()


                    _uiState.value = TotpUiState.Success(
                        code = code,
                        timerValue = timerValue
                    )
                    delay(1000)
                }catch (e: Exception){
                   _uiState.value = TotpUiState.Error("Ошибка: ${e.message}")
                }
            }
        }
    }

    fun stop(){
        timerJob?.cancel()
        _uiState.value = TotpUiState.Idle
    }

}