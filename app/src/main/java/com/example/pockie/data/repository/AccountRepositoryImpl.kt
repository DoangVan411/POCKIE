package com.example.pockie.data.repository

import android.content.Context
import android.net.Uri
import com.example.pockie.data.source.remote.FirebaseUserDataSource
import com.example.pockie.data.source.remote.SupabasePostDataSource
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val firebaseUserDataSource: FirebaseUserDataSource,
    private val supabasePostDataSource: SupabasePostDataSource
) : AccountRepository {
    override fun getAccounts(): Flow<NetworkState> {
        return firebaseUserDataSource.getAllAccounts()
    }

    override suspend fun getAccount(uid: String): Account {
        return firebaseUserDataSource.getAccount(uid)
    }

    override fun editProfile(fullName: String, bio: String, avtUrl: String): Flow<NetworkState> {
        return firebaseUserDataSource.editAccount(fullName, bio, avtUrl)
    }

    override fun generateAvtLinkPhoto(
        fileName: String,
        uri: Uri,
        context: Context
    ): Flow<NetworkState> {
        return supabasePostDataSource.generateAvtLinkPhoto(fileName, uri, context)
    }
}