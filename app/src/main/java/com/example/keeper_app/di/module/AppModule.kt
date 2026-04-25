package com.example.keeper_app.di.module

import android.app.Application
import android.content.Context
import com.example.keeper_app.data.network.totp.CryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApplication(application: Application): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManager()
}