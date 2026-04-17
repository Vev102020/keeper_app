package com.example.keeper_app.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.keeper_app.data.storage.entities.ServiceDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    // Получить все
    @Query("SELECT * FROM service_table WHERE userId = :userId ORDER BY name")
    fun getServicesForUser(userId: String) : Flow<List<ServiceDb>>

    // Добавить
    @Insert
    suspend fun  insertService(service: ServiceDb) : Long

    // Обновить
    @Update
    suspend fun updateService(service: ServiceDb)

    // Удалить сервис
    @Delete
    suspend fun deleteService(service: ServiceDb)

    // Удалить все для пользователя
    @Query("DELETE FROM service_table WHERE userId = :userId")
    suspend fun deleteAllServicesForUser(userId: String)
}