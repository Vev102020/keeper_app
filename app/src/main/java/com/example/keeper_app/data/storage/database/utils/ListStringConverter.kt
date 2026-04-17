package com.example.keeper_app.data.storage.database.utils

import androidx.room.TypeConverter

class ListStringConverter {

    @TypeConverter
    fun fromString(stringListString: String) : List<String>{
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>) : String{
        return stringList.joinToString(separator = ",")
    }
}