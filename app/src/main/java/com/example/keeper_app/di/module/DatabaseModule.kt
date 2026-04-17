package com.example.keeper_app.di.module

import android.content.Context
import androidx.room.Room
import com.example.keeper_app.data.storage.dao.ServiceDao
import com.example.keeper_app.data.storage.dao.UserDao
import com.example.keeper_app.data.storage.database.AppDatabase
import com.example.keeper_app.data.storage.database.AppDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao{
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideServiceDao(db: AppDatabase) : ServiceDao {
        return db.serviceDao()
    }
}