package com.example.keeper_app.data.network.session

import com.example.keeper_app.data.network.repo.AuthRepository
import com.example.keeper_app.data.storage.entities.UserDb
import com.example.keeper_app.presentation.auth.viewmodel.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend fun getCurrentUser() : UserDb? {
        return authRepository.getCurrentUser()
    }

    suspend fun getUserId() : String? {
        return getCurrentUser()?.firebaseId
    }
    suspend fun requireUserId() : String {
        return getUserId() ?: throw IllegalStateException("Пользователь не авторизирован")
    }

    suspend fun isLoggerIn() : Boolean{
        return getCurrentUser() != null
    }

    fun observeUser(): Flow<UserDb?> {
        return authRepository.authState
            .map { state ->
                when (state) {
                    is AuthState.Success -> state.user
                    else -> null
                }
            }
    }

    suspend fun logout(){
        authRepository.logout()
    }
}