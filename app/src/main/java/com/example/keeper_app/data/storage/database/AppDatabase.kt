package com.example.keeper_app.data.storage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keeper_app.data.storage.dao.ServiceDao
import com.example.keeper_app.data.storage.dao.UserDao
import com.example.keeper_app.data.storage.database.utils.ListStringConverter
import com.example.keeper_app.data.storage.database.utils.TotpConverter
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.data.storage.entities.UserDb

@Database(
    entities = [UserDb::class, ServiceDb::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(ListStringConverter::class, TotpConverter::class)
abstract class AppDatabase : RoomDatabase (){
    abstract fun userDao() : UserDao
    abstract fun serviceDao() : ServiceDao

    companion object{
        const val DATABASE_NAME = "keeper.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}