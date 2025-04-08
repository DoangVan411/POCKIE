package com.example.pockie.domain.repository

import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<NetworkState>
    suspend fun getAccount(uid: String): Account
}
