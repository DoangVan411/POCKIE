package com.example.pockie.domain.repository

import android.content.Context
import android.net.Uri
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<NetworkState>
    suspend fun getAccount(uid: String): Account
    fun editProfile(fullName: String, bio: String, avtUrl: String): Flow<NetworkState>
    fun generateAvtLinkPhoto(fileName: String, uri: Uri, context: Context): Flow<NetworkState>
}
