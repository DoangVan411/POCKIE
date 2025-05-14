package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Post
import com.example.pockie.domain.repository.PostRepository
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(post: Post) = postRepository.updatePost(post)

}