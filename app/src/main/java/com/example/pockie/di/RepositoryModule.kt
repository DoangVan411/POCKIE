package com.example.pockie.di

import com.example.pockie.data.repository.AccountRepositoryImpl
import com.example.pockie.data.repository.ChatRepositoryImpl
import com.example.pockie.data.source.remote.FirebaseChatDataSource
import com.example.pockie.data.source.remote.FirebaseUserDataSource
import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.domain.repository.ChatRepository
import com.example.pockie.data.repository.AuthRepositoryImpl
import com.example.pockie.data.repository.FriendRepositoryImpl
import com.example.pockie.data.source.remote.FirebaseFriendDataSource
import com.example.pockie.domain.repository.AuthRepository
import com.example.pockie.domain.repository.FriendRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository{
        return AuthRepositoryImpl(firebaseAuth, firestore)
    }

    @Provides
    @Singleton
    fun provideFriendRepository(firebaseFriendDataSource: FirebaseFriendDataSource): FriendRepository {
        return FriendRepositoryImpl(firebaseFriendDataSource)
    }

}