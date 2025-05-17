package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.AccountRepository
import javax.inject.Inject

class EditAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(fullName: String, bio: String, avtUrl: String) =
        accountRepository.editProfile(fullName, bio, avtUrl)
}