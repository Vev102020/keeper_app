package com.example.keeper_app.presentation.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keeper_app.AppViewModel
import com.example.keeper_app.data.network.repo.AuthRepository
import com.example.keeper_app.data.network.session.SessionManager
import com.example.keeper_app.data.storage.dao.ServiceDao
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.data.storage.entities.Totp
import com.example.keeper_app.presentation.auth.viewmodel.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainState {
    data object Loading : MainState
    data object Idle : MainState
    data class Success(val service: List<ServiceDb>) : MainState
    data class Error(val message: String) : MainState
}

data class AddServiceState(
    val serviceName: String = "",
    val secretKey: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val serviceDao: ServiceDao,
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository

) : ViewModel() {
    private companion object {
        const val TAG = "MainViewModel"
    }

    private val _mainState = MutableStateFlow<MainState>(MainState.Idle)
    val mainState: StateFlow<MainState> = _mainState.asStateFlow()

    private val _addServiceState = MutableStateFlow(AddServiceState())
    val addServiceState = _addServiceState.asStateFlow()


    init {
        loadServices()
    }

    private fun loadServices(){
        viewModelScope.launch {
            _mainState.value = MainState.Loading

            try {
                val userId = sessionManager.requireUserId()

                serviceDao.getServicesForUser(userId)
                    .map { services ->
                       MainState.Success(services)
                    }
                    .catch { exception ->
                        Log.e(TAG, "Ошибка загрузки сервисов", exception)
                        _mainState.value = MainState.Error(exception.message ?: "Ошибка загрузки")
                    }
                    .collect { state ->
                        _mainState.value = state
                    }
            }catch (e: Exception){
                Log.e(TAG, "Ошибка в loadServices", e)
                authRepository.setAuthState(AuthState.Idle)
                _mainState.value = MainState.Error("Пользователь не авторизирован")
            }

        }
    }

    fun updateServiceName(name: String) {
        _addServiceState.update { it.copy(serviceName = name) }
    }
    fun updateSecretKey(secret: String) {
        _addServiceState.update { it.copy(secretKey = secret.uppercase()) }
    }
    fun clearAddServiceForm() {
        _addServiceState.value = AddServiceState()
    }

    fun addService(){
        viewModelScope.launch {
            _addServiceState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val userId = sessionManager.requireUserId()
                val name = _addServiceState.value.serviceName
                val secretKey = _addServiceState.value.secretKey.uppercase()

                if (name.isBlank()) {
                    _addServiceState.update {
                        it.copy(isLoading = false, errorMessage = "Название не может быть пустым")
                    }
                    return@launch
                }

                if (secretKey.isBlank()) {
                    _addServiceState.update {
                        it.copy(isLoading = false, errorMessage = "Ключ не может быть пустым")
                    }
                    return@launch
                }

                if (!isValidBase32(secretKey)) {
                    _addServiceState.update {
                        it.copy(isLoading = false, errorMessage = "Некорректный формат ключа (Base32)")
                    }
                    return@launch
                }

                val totp = Totp(secretKey = secretKey)
                val service = ServiceDb(
                    id = 0,
                    userId = userId,
                    name = name,
                    totp = totp
                )

                serviceDao.insertService(service)

                _addServiceState.update {
                    it.copy(isLoading = false, success = true)
                }

                clearAddServiceForm()
            } catch (e: Exception){
                _addServiceState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Ошибка: ${e.message}"
                    )
                }
            }

        }

    }

    private fun isValidBase32(secret: String): Boolean {
        if (!"^[A-Z2-7]+=*$".toRegex().matches(secret)) return false
        // Длина должна быть кратна 8
        return secret.length % 8 == 0
    }
}