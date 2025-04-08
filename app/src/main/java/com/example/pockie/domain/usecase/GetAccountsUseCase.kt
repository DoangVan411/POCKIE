package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(private val accountRepository: AccountRepository){
    suspend operator fun invoke(): Flow<List<Account>> {
        return accountRepository.getAccounts()
    }
}