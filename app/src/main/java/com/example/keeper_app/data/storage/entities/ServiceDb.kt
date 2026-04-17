package com.example.keeper_app.data.storage.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.keeper_app.data.storage.database.utils.TotpConverter

data class Totp(
    val secretKey: String,
    val algorithm: String = "SHA1", //тип шифра
    val digits: Int = 6, //длина символов
    val period: Int = 30, //диапазон генерации 30сек
)

@Entity(
    tableName = "service_table",
    foreignKeys = [
        ForeignKey(
            entity = UserDb::class,
            parentColumns = ["firebaseId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // каскадное удаление при удалении пользователя
        )
    ],
    indices = [
        Index(value = ["userId"])
    ]
)

data class ServiceDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String, //id пользователя
    val name: String,
    @TypeConverters(TotpConverter::class)
    val totp: Totp
)
