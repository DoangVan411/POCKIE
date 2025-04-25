package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): String? {
        return authRepository.getCurrentUseId()
    }
}