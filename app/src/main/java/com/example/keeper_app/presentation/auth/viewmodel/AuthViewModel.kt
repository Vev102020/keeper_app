package com.example.keeper_app.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keeper_app.data.storage.entities.UserDb
import com.example.keeper_app.domain.usecases.auth.LoginUseCase
import com.example.keeper_app.domain.usecases.auth.RegisterUseCase
import com.example.keeper_app.domain.usecases.auth.SendPasswordResetEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthState {
    data object Loading : AuthState
    data object Idle : AuthState
    data class Success(val user: UserDb) : AuthState
    data class Error(val message: String) : AuthState
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showPassword: Boolean = false
)

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showPassword: Boolean = false
)

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)



@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {
    private companion object{
        const val MIN_LENGTH_PASSWORD = 6
    }
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    private val _forgotPasswordState = MutableStateFlow(ForgotPasswordState())
    val forgotPasswordState: StateFlow<ForgotPasswordState> = _forgotPasswordState.asStateFlow()


    fun login(
        email: String,
        password: String
    ){
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, error = null) }

            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = {user ->
                    _loginState.update { it.copy(isLoading = false) }
                    _authState.value = AuthState.Success(user)
                },
                onFailure = {error ->
                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Ошибка входа"
                        )
                    }
                }
            )
        }
    }


    fun register(
        email: String,
        password: String,
        confirmPassword: String
    ){
        viewModelScope.launch {
            _registerState.update { it.copy(isLoading = true, error = null) }

            //Валидация
            if(password != confirmPassword){
                _registerState.update { it.copy(
                    isLoading = false,
                    error = "Пароли не совпадают"
                ) }
                return@launch
            }

            if (password.length < MIN_LENGTH_PASSWORD){
                _registerState.update { it.copy(
                    isLoading = false,
                    error = "Минимальная длина пароля $MIN_LENGTH_PASSWORD символов"
                ) }
                return@launch
            }

            val result = registerUseCase(email, password)

            result.fold(
                onSuccess = {user ->
                    _registerState.update { it.copy(isLoading = false) }
                    _authState.value = AuthState.Success(user)
                },
                onFailure = {error ->
                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Ошибка регистрации"
                        )
                    }
                }
            )
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _forgotPasswordState.update { it.copy(isLoading = true, error = null, successMessage = null) }

            // Валидация email (опционально, но желательно)
            if (email.isBlank()) {
                _forgotPasswordState.update {
                    it.copy(
                        isLoading = false,
                        error = "Введите email"
                    )
                }
                return@launch
            }

            val result = sendPasswordResetEmailUseCase(email)

            if (result == null) {
                _forgotPasswordState.update {
                    it.copy(
                        isLoading = false,
                        error = "Не удалось отправить письмо"
                    )
                }
                return@launch
            }

            result.fold(
                onSuccess = {
                    _forgotPasswordState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Ссылка для восстановления пароля отправлена на указанную почту"
                        )
                    }
                },
                onFailure = { error ->
                    _forgotPasswordState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Не удалось отправить письмо"
                        )
                    }
                }
            )
        }
    }

    fun updateLoginState(
        email: String = loginState.value.email,
        password: String = loginState.value.password
    ){
        _loginState.update {
            it.copy(
                email = email,
                password = password
            )
        }
    }

    fun updateRegisterState(
        email: String = registerState.value.email,
        password: String = registerState.value.password,
        confirmPassword: String = registerState.value.confirmPassword
    ){
        _registerState.update {
            it.copy(
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
        }
    }

    fun updateForgotPasswordState(
        email: String = forgotPasswordState.value.email
    ){
        _forgotPasswordState.update { it.copy(
            email = email
        ) }
    }

    fun toggleShowPassword(){
        _loginState.update { it.copy(
            showPassword = !it.showPassword
        ) }
    }

    fun clearLoginError(){
        _loginState.update { it.copy(error = null) }
    }

    fun clearRegisterError(){
        _registerState.update { it.copy(error = null) }
    }

    fun clearForgotPasswordError(){
        _forgotPasswordState.update { it.copy(error = null) }
    }

}