package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.FriendRepository
import javax.inject.Inject

class AcceptFriendRequestUseCase @Inject constructor(private val friendRepository: FriendRepository) {
    suspend operator fun invoke(senderId: String, receiverId: String) {
        return friendRepository.acceptFriendRequest(senderId, receiverId)
    }
}