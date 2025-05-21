package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AuthRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(account: Account): NetworkState {
        return authRepository.deleteAccount(account)
    }
}