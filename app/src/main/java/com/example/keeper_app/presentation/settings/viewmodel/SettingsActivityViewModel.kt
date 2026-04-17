package com.example.keeper_app.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class SettingsUiState {
    object Idle : SettingsUiState()
    object Loading : SettingsUiState()
    object LogoutSuccess : SettingsUiState()
    object DeleteSuccess : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}


@HiltViewModel
class SettingsActivityViewModel @Inject constructor(

): ViewModel(){
    private companion object{
        const val TAG = "SettingsActivityViewModel"
    }

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Idle)
    val uiState : StateFlow<SettingsUiState> = _uiState.asStateFlow()



}