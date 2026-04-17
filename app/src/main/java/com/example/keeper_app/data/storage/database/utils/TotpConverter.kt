package com.example.keeper_app.data.storage.database.utils

import androidx.room.TypeConverter
import com.example.keeper_app.data.storage.entities.Totp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TotpConverter {
    private val gson = Gson()

    @TypeConverter
    fun toJson(totp: Totp?) : String?{
        return totp?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun fromJson(json: String?) : Totp?{
        return json?.let {
            gson.fromJson(it, object : TypeToken<Totp>() {}.type)
        }
    }
}