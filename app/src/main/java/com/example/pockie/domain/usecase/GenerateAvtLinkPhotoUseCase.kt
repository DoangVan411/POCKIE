package com.example.pockie.domain.usecase

import android.content.Context
import android.net.Uri
import com.example.pockie.domain.repository.AccountRepository
import javax.inject.Inject

class GenerateAvtLinkPhotoUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(fileName: String, uri: Uri, context: Context) =
        accountRepository.generateAvtLinkPhoto(fileName, uri, context)
}