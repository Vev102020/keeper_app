package com.example.keeper_app.di.module

import com.example.keeper_app.data.network.repo.AuthRepository
import com.example.keeper_app.data.network.repo.AuthRepositoryImpl
import com.example.keeper_app.data.network.repo.PinRepository
import com.example.keeper_app.data.network.repo.PinRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindPinRepository(
        impl: PinRepositoryImpl
    ): PinRepository
}