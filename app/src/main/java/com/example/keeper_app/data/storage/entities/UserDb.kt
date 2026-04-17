package com.example.keeper_app.data.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDb(
    @PrimaryKey
    val firebaseId : String,
    val email: String? = null,
    val createdAt: Long = System.currentTimeMillis(), // время создания
    val lastLogin: Long? = null,  // время последнего входа
    val pinHash: String? = null,  // хеш PIN‑кода
    val isPinEnabled: Boolean = false  // включён ли PIN
)
