package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.FriendRepository
import javax.inject.Inject

class SendRequestUseCase @Inject constructor(private val friendRepository: FriendRepository) {
    suspend operator fun invoke(senderId: String, receiverId: String) {
        return friendRepository.sendRequest(senderId, receiverId)
    }
}