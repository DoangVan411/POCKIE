package com.example.pockie.domain.repository

import com.example.pockie.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccounts(): Flow<List<Account>>
    suspend fun getAccount(uid: String): Account
}
