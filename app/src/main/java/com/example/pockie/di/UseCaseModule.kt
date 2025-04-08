package com.example.pockie.di

import com.example.pockie.domain.repository.AuthRepository
import com.example.pockie.domain.usecases.LoginUseCase
import com.example.pockie.domain.usecases.RegisterUseCase
import com.example.pockie.domain.usecases.ResetPasswordUseCase
import com.example.pockie.domain.usecases.SaveAccountUseCase
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
    fun provideRegisterUseCase(
        repository: AuthRepository
    ): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(
        repository: AuthRepository
    ): LoginUseCase{
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveAccountUseCase(
        repository: AuthRepository
    ): SaveAccountUseCase{
        return SaveAccountUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(
        repository: AuthRepository
    ): ResetPasswordUseCase{
        return ResetPasswordUseCase(repository)
    }
} 