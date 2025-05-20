package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.PostRepository
import javax.inject.Inject

class GetPostUserUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(uid: String) = postRepository.getPostUser(uid)
}