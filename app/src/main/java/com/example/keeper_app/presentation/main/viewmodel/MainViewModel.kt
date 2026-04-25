package com.example.keeper_app.presentation.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keeper_app.data.network.repo.AuthRepository
import com.example.keeper_app.data.network.repo.TotpRepository
import com.example.keeper_app.data.network.session.SessionManager
import com.example.keeper_app.data.storage.dao.ServiceDao
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.data.storage.entities.Totp
import com.example.keeper_app.presentation.auth.viewmodel.AuthState
import com.example.keeper_app.presentation.main.state.UiDialog
import com.example.keeper_app.presentation.main.ui.screen.parseOtpAuth
import com.journeyapps.barcodescanner.ScanIntentResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

sealed class NavigationEvent{
    object NavigateBack: NavigationEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val serviceDao: ServiceDao,
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository,
    private val totpRepository: TotpRepository

) : ViewModel() {
    private companion object {
        const val TAG = "MainViewModel"
    }

    private val _mainState = MutableStateFlow<MainState>(MainState.Idle)
    val mainState: StateFlow<MainState> = _mainState.asStateFlow()

    private val _addServiceState = MutableStateFlow(AddServiceState())
    val addServiceState = _addServiceState.asStateFlow()

    private val _scanIntent = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val scanIntent : SharedFlow<Unit> = _scanIntent.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    private val _uiDialog = MutableStateFlow<UiDialog>(UiDialog.Idle)
    val uiDialog : StateFlow<UiDialog> = _uiDialog.asStateFlow()


    init {
        loadServices()
    }

    //Изменяем состояния полей
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
                      _mainState.value = MainState.Error(exception.message ?: "Ошибка загрузки")
                    }
                    .collect { state ->
                        _mainState.value = state
                    }
            }catch (e: Exception){
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

    //Работа сканера через камеру
    fun startScan() {
        viewModelScope.launch {
            _scanIntent.emit(Unit)
        }
    }

    fun handleScanResult(result: ScanIntentResult) {
        if(result.contents != null){
            val content = result.contents
            val otpData = parseOtpAuth(content)
            val secret = otpData?.secret ?: content.uppercase()
            updateSecretKey(secret)

            val currentName = addServiceState.value.serviceName
            if (currentName.isBlank() && otpData?.issuer != null) {
                updateServiceName(otpData.issuer)
            }

        }else{
            Log.i(TAG, "Отмена сканирования")
        }
    }


    //Диалоги
    fun showRenameDialog(service: ServiceDb){
        _uiDialog.value = UiDialog.Rename(service)
    }
    fun showDetailDialog(service: ServiceDb){
        _uiDialog.value = UiDialog.Detail(service)
    }
    fun showDeleteDialog(service: ServiceDb){
        _uiDialog.value = UiDialog.Delete(service)
    }
    fun hideDialog(){
        _uiDialog.value = UiDialog.Idle
    }
    //Операции с диалогами
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

                totpRepository.saveServiceWithTotp(name, secretKey, userId)
                _addServiceState.update {
                    it.copy(isLoading = false, success = true)
                }

                _navigationEvent.emit(NavigationEvent.NavigateBack)

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

    fun renameService(newName: String){
        viewModelScope.launch {
            val dialog = _uiDialog.value
            if(dialog !is UiDialog.Rename) return@launch

            try {
                // если не пустое
                if(newName.isBlank()){
                    _uiDialog.value = dialog.copy(message = "Название сервиса не может быть пустым")
                    return@launch
                }

                // если отличается от текущего
                if(newName == dialog.service.name){
                    _uiDialog.value = dialog.copy(message = "Новое название сервиса должно отличаться от текущего")
                    return@launch
                }

                // проверка уникальности имени
                val services = when(val state = _mainState.value){
                    is MainState.Success -> state.service
                    else -> emptyList()
                }


                if (services.any{it.name == newName && it.id != dialog.service.id}){
                    _uiDialog.value = dialog.copy(message = "Сервис с таким именем существует")
                    return@launch
                }

                val newServices = dialog.service.copy(name = newName)

                serviceDao.updateService(newServices)
                hideDialog()
                Log.i(TAG, "Сервис переименован")
            }catch (e: Exception){
                Log.e(TAG, "Не удалось переименовать сервис. Ошибка: ${e.message}")
                _uiDialog.value = dialog.copy(message =  "Не удалось переименовать сервис")
            }
        }
    }

    fun deleteService(){
        viewModelScope.launch {
            val dialog = _uiDialog.value
            if (dialog !is UiDialog.Delete) return@launch

            serviceDao.deleteService(dialog.service)
            _mainState.value = when(val state = _mainState.value){
                is MainState.Success -> MainState.Success(state.service.filter { it.id != dialog.service.id })
                else -> state
            }

            hideDialog()
        }
    }

}