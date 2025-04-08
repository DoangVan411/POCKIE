package com.example.pockie.di

import com.example.pockie.data.repository.AccountRepositoryImpl
import com.example.pockie.data.repository.ChatRepositoryImpl
import com.example.pockie.data.source.remote.FirebaseChatDataSource
import com.example.pockie.data.source.remote.FirebaseUserDataSource
import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideChatRepository(firebaseChatDataSource: FirebaseChatDataSource): ChatRepository {
        return ChatRepositoryImpl(firebaseChatDataSource)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(firebaseUserDataSource: FirebaseUserDataSource): AccountRepository {
        return AccountRepositoryImpl(firebaseUserDataSource)
    }
}