package com.example.pockie.domain.repository

import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(email: String, password: String): Flow<NetworkState>
    fun login(email: String, passwd: String): Flow<NetworkState>
    fun saveAccountToFireStore(account: Account): Flow<NetworkState>
    fun resetPassword(email: String): Flow<NetworkState>
    suspend fun getCurrentUseId(): String?
}