package com.example.keeper_app.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.keeper_app.data.storage.entities.UserDb

@Dao
interface UserDao {
    // Получить все
    @Query("SELECT * FROM user_table WHERE firebaseId = :firebaseId")
    suspend fun getUserByFirebaseId(firebaseId: String): UserDb?

    // Добавить
    @Insert
    suspend fun insertUser(user: UserDb)

    // Обновить
    @Update
    suspend fun updateUser(user: UserDb)

    // Обновить время последнего входа
    @Query("UPDATE user_table SET lastLogin = :timestamp WHERE firebaseId = :firebaseId")
    suspend fun updateLastLogin(firebaseId: String, timestamp: Long)

    // Удалить пользователя
    @Query("DELETE FROM user_table WHERE firebaseId = :firebaseId")
    suspend fun deleteUserByFirebase(firebaseId: String) : Int

    @Query("UPDATE user_table Set pinHash = :newPinHash WHERE firebaseId = :firebaseId")
    suspend fun updateUserPinHash(firebaseId: String, newPinHash: String)
}