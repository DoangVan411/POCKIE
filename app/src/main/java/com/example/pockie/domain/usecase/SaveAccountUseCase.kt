package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AuthRepository
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(account: Account)= authRepository.saveAccountToFireStore(account)
}