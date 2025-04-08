package com.example.pockie.di

import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.domain.repository.ChatRepository
import com.example.pockie.domain.usecase.GetAccountUseCase
import com.example.pockie.domain.usecase.GetAccountsUseCase
import com.example.pockie.domain.usecase.GetMessagesUseCase
import com.example.pockie.domain.usecase.SendMessageUseCase
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
    fun provideGetAccountUseCase(accountRepository: AccountRepository): GetAccountUseCase {
        return GetAccountUseCase(accountRepository)
    }

    @Provides
    @Singleton
    fun provideGetAccountsUseCase(accountRepository: AccountRepository): GetAccountsUseCase {
        return GetAccountsUseCase(accountRepository)
    }

    @Provides
    @Singleton
    fun provideGetMessagesUseCase(chatRepository: ChatRepository): GetMessagesUseCase {
        return GetMessagesUseCase(chatRepository)
    }

    @Provides
    @Singleton
    fun provideSendMessageUseCase(chatRepository: ChatRepository): SendMessageUseCase {
        return SendMessageUseCase(chatRepository)
    }
}