package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.PostRepository
import javax.inject.Inject

class GetAllPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke() = postRepository.getAllPost()
}