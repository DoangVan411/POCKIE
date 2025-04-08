package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(uid: String): Account {
        return accountRepository.getAccount(uid)
    }
}