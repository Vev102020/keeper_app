package com.example.keeper_app.di.module

import com.example.keeper_app.data.network.repo.AuthRepository
import com.example.keeper_app.domain.usecases.auth.CheckAuthStatusUseCase
import com.example.keeper_app.domain.usecases.auth.GetCurrentUserUseCase
import com.example.keeper_app.domain.usecases.auth.LoginUseCase
import com.example.keeper_app.domain.usecases.auth.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository) : LoginUseCase{
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository) : RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCheckAuthStatusUseCase(repository: AuthRepository) : CheckAuthStatusUseCase {
        return CheckAuthStatusUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(repository: AuthRepository) : GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository)
    }
}