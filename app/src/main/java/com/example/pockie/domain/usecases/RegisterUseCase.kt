package com.example.pockie.domain.usecases

import com.example.pockie.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, passwd: String) = authRepository.register(email, passwd)
}