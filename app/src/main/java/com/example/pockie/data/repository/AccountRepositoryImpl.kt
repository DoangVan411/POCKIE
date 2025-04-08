package com.example.pockie.data.repository

import com.example.pockie.data.source.remote.FirebaseUserDataSource
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val firebaseUserDataSource: FirebaseUserDataSource): AccountRepository {
    override fun getAccounts(): Flow<List<Account>> {
        return firebaseUserDataSource.getAccounts()
    }

    override suspend fun getAccount(uid: String): Account {
        return firebaseUserDataSource.getAccount(uid)
    }
}