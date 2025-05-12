package com.example.pockie.di

import com.example.pockie.BuildConfig
import com.example.pockie.data.source.remote.FirebaseChatDataSource
import com.example.pockie.data.source.remote.FirebaseFriendDataSource
import com.example.pockie.data.source.remote.FirebasePostDataSource
import com.example.pockie.data.source.remote.FirebaseUserDataSource
import com.example.pockie.data.source.remote.SupabasePostDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
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
    fun provideSupabaseStorage(): Storage{
        val client = createSupabaseClient(
            supabaseUrl = BuildConfig.URL,
            supabaseKey = BuildConfig.API_KEY
        ){
            install(io.github.jan.supabase.storage.Storage)
        }
        return client.storage
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
    fun provideSupabsePostDataSource(storage: Storage): SupabasePostDataSource{
        return SupabasePostDataSource(storage)
    }

    @Provides
    @Singleton
    fun provideFirebaseFriendDataSource(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth, firebaseUserDataSource: FirebaseUserDataSource): FirebaseFriendDataSource {
        return FirebaseFriendDataSource(firestore, firebaseAuth, firebaseUserDataSource)
    }

    @Provides
    @Singleton
    fun provideFirebasePostDataSource(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): FirebasePostDataSource{
        return FirebasePostDataSource(firebaseAuth, firestore)
    }
}

