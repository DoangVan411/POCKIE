package com.example.pockie.di

import com.example.pockie.data.source.remote.FirebaseChatDataSource
import com.example.pockie.data.source.remote.FirebaseFriendDataSource
import com.example.pockie.data.source.remote.FirebaseUserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging{
        return FirebaseMessaging.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseChatDataSource(firestore: FirebaseFirestore): FirebaseChatDataSource {
        return FirebaseChatDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseUserDataSource(firestore: FirebaseFirestore): FirebaseUserDataSource {
        return FirebaseUserDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseFriendDataSource(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth, firebaseUserDataSource: FirebaseUserDataSource): FirebaseFriendDataSource {
        return FirebaseFriendDataSource(firestore, firebaseAuth, firebaseUserDataSource)
    }
}

