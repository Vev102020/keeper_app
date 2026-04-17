package com.example.keeper_app

sealed class AppState {
    object Unauthenticated : AppState()
    data class Authenticated(val userId: String, val requiresPin: Boolean = false) : AppState()
    data class PinRequired(val userId: String, val isNewPin: Boolean = false) : AppState()
    object Logout : AppState()
    object ForceLogout : AppState()
    object ChangePin : AppState()
}