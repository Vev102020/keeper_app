package com.example.keeper_app.presentation.about.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class AboutUiState {
    object Idle : AboutUiState()
    object Loading : AboutUiState()
    object LogoutSuccess : AboutUiState()
    object DeleteSuccess : AboutUiState()
    data class Error(val message: String) : AboutUiState()
}


class AboutViewModel @Inject constructor(

) : ViewModel() {
    private companion object{
        const val TAG = "AboutViewModel"
    }

    private val _uiState = MutableStateFlow<AboutUiState>(AboutUiState.Idle)
    val uiState : StateFlow<AboutUiState> = _uiState.asStateFlow()


}