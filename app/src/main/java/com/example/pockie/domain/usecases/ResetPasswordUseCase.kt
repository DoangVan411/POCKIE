package com.example.pockie.domain.usecases

import com.example.pockie.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String) = authRepository.resetPassword(email)
}