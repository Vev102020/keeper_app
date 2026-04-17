package com.example.keeper_app.data.network.repo

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


interface PinRepository {
    suspend fun hasPin(userId: String): Boolean
    suspend fun verifyPin(userId: String, pin: String): Boolean
    suspend fun setPin(userId: String, pin: String)
    suspend fun changePin(userId: String, oldPin: String, newPin: String): Boolean
    suspend fun deletePin(userId: String)
}

@Singleton
class PinRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PinRepository{
    override suspend fun hasPin(userId: String): Boolean {
        return false
    }

    override suspend fun verifyPin(userId: String, pin: String): Boolean {
        return false
    }

    override suspend fun setPin(userId: String, pin: String) {
        TODO("Not yet implemented")
    }

    override suspend fun changePin(
        userId: String,
        oldPin: String,
        newPin: String
    ): Boolean {
        return false
    }

    override suspend fun deletePin(userId: String) {
        TODO("Not yet implemented")
    }
}