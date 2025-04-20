package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.FriendRepository
import javax.inject.Inject

class DeleteFriendUseCase @Inject constructor(private val friendRepository: FriendRepository) {
    suspend operator fun invoke(friendId: String) {
        return friendRepository.deleteFriend(friendId)
    }
}