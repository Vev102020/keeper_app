package com.example.keeper_app.presentation.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keeper_app.data.network.repo.TotpRepository
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
class DetailServiceViewModel @Inject constructor(
    private val generateTotpUseCase: GenerateTotpUseCase,
    private val totpRepository: TotpRepository
) : ViewModel() {
    private companion object{
        const val TAG = "DetailServiceViewModel"
    }

    private val _uiState = MutableStateFlow<TotpUiState>(TotpUiState.Idle)
    val uiState: StateFlow<TotpUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    //Расшифровака кода
    suspend fun decryptTotp(totp: Totp): Totp? = try {
        totpRepository.decryptTotp(totp)
    } catch (e: Exception) {
        Log.e(TAG, "Расшифровка не удалась", e)
        null
    }

    fun startTotpGeneration(decryptedTotp: Totp){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive){
                try {
                    val code = generateTotpUseCase.generateTotp(decryptedTotp)
                    Log.d(TAG, "Сгенерирован код: $code")
                    val currentTime = System.currentTimeMillis() / 1000
                    val timerValue = (decryptedTotp.period - (currentTime % decryptedTotp.period)).toInt()

                    Log.d(TAG, "Устанавливаем Success: code=$code, timer=$timerValue")
                    _uiState.value = TotpUiState.Success(
                        code = code,
                        timerValue = timerValue
                    )
                    delay(1000)
                }catch (e: Exception){
                    Log.e(TAG, "Ошибка генерации", e)
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