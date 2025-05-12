package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.PostRepository
import java.io.File
import javax.inject.Inject

class GenerateLinkPhotoUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(fileName: String, photoFile: File) = postRepository.generateLinkPhoto(fileName, photoFile)
}