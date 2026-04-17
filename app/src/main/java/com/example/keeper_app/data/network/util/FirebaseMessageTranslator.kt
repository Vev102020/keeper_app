package com.example.keeper_app.data.network.util

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

object FirebaseMessageTranslator {

    fun translateErrorMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> {
                when (exception.errorCode) {
                    "ERROR_INVALID_EMAIL" -> "Неверный формат email"
                    "ERROR_WRONG_PASSWORD" -> "Неверный пароль"
                    "ERROR_INVALID_CREDENTIAL" -> "Неверные учетные данные"
                    else -> {
                        val message = exception.message ?: "неизвестная ошибка"
                        // Проверяем сообщение об ошибке
                        when (message) {
                            "The supplied auth credential is malformed or has expired." ->
                                "Учетные данные повреждены или устарели. Войдите заново"
                            else -> "Ошибка авторизации: $message"
                        }
                    }
                }
            }
            is FirebaseAuthUserCollisionException -> {
                "Аккаунт с таким email уже существует"
            }
            is FirebaseAuthWeakPasswordException -> {
                "Пароль слишком слабый. Используйте минимум 6 символов"
            }
            is FirebaseAuthInvalidUserException -> {
                when (exception.errorCode) {
                    "ERROR_USER_NOT_FOUND" -> "Пользователь не найден"
                    "ERROR_USER_DISABLED" -> "Аккаунт отключен"
                    else -> "Неверный пользователь"
                }
            }
            is FirebaseAuthEmailException -> {
                val message = exception.message ?: "неизвестная ошибка"
                "Ошибка с email: $message"
            }
            is FirebaseNetworkException -> {
                "Проблемы с сетью. Проверьте подключение"
            }
            is FirebaseTooManyRequestsException -> {
                "Слишком много запросов. Попробуйте позже"
            }
            else -> {
                val message = exception.message ?: "неизвестная ошибка"
                // Общие ошибки
                when (message) {
                    "The email address is already in use by another account." ->
                        "Этот email уже используется другим аккаунтом"
                    "The password is invalid or the user does not have a password." ->
                        "Неверный пароль или у пользователя нет пароля"
                    "There is no user record corresponding to this identifier. The user may have been deleted." ->
                        "Пользователь не найден. Возможно, аккаунт был удален"
                    "The user account has been disabled by an administrator." ->
                        "Аккаунт отключен администратором"
                    "Network error (such as timeout, interrupted connection or unreachable host) has occurred." ->
                        "Ошибка сети. Проверьте подключение к интернету"
                    "We have blocked all requests from this device due to unusual activity. Try again later." ->
                        "Запросы с этого устройства заблокированы из-за подозрительной активности"
                    else -> message
                }
            }
        }
    }
}