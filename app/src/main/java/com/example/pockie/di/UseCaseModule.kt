package com.example.pockie.di

import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.domain.repository.AuthRepository
import com.example.pockie.domain.repository.ChatRepository
import com.example.pockie.domain.repository.FriendRepository
import com.example.pockie.domain.usecase.GetAccountUseCase
import com.example.pockie.domain.usecase.GetAccountsUseCase
import com.example.pockie.domain.usecase.GetCurrentUserIdUseCase
import com.example.pockie.domain.usecase.GetFriendsUseCase
import com.example.pockie.domain.usecase.GetMessagesUseCase
import com.example.pockie.domain.usecase.GetReceivedRequestsUseCase
import com.example.pockie.domain.usecase.GetSentRequestsUseCase
import com.example.pockie.domain.usecase.SendMessageUseCase
import com.example.pockie.domain.usecase.LoginUseCase
import com.example.pockie.domain.usecase.RegisterUseCase
import com.example.pockie.domain.usecase.ResetPasswordUseCase
import com.example.pockie.domain.usecase.SaveAccountUseCase
import com.example.pockie.domain.usecase.SearchUserUseCase
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
    ): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveAccountUseCase(
        repository: AuthRepository
    ): SaveAccountUseCase {
        return SaveAccountUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(
        repository: AuthRepository
    ): ResetPasswordUseCase {
        return ResetPasswordUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchUserUseCase (
        accountRepository: AccountRepository,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    ): SearchUserUseCase {
        return SearchUserUseCase(accountRepository, getCurrentUserIdUseCase)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserIdUseCase (
        authRepository: AuthRepository
    ): GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetFriendsUseCase (
        friendRepository: FriendRepository
    ): GetFriendsUseCase {
        return GetFriendsUseCase(friendRepository)
    }

    @Provides
    @Singleton
    fun provideGetSentRequestsUseCase (
        accountRepository: AccountRepository,
        friendRepository: FriendRepository
    ): GetSentRequestsUseCase {
        return GetSentRequestsUseCase(accountRepository, friendRepository)
    }

    @Provides
    @Singleton
    fun provideGetReceivedRequestsUseCase (
        accountRepository: AccountRepository,
        friendRepository: FriendRepository
    ): GetReceivedRequestsUseCase {
        return GetReceivedRequestsUseCase(accountRepository, friendRepository)
    }
}