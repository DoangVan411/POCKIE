package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.repository.ChatRepository
import com.example.pockie.presentation.utils.Utils.getChatId
import javax.inject.Inject

class GetMessagesOnceUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val getCurrentUserId: GetCurrentUserIdUseCase
) {
    suspend operator fun invoke(friendId: String): Chat? {
        val messages = mutableListOf<Chat>()
        val messagesSent = getCurrentUserId()?.let { getChatId(it, friendId) }
            ?.let { chatRepository.getMessagesOnce(it) }
        val messagesReceived = getCurrentUserId()?.let { getChatId(friendId, it) }
            ?.let { chatRepository.getMessagesOnce(it) }
        if (messagesSent != null) {
            messages.addAll(messagesSent)
        }
        if(messagesReceived != null) {
            messages.addAll(messagesReceived)
        }
        return messages.maxByOrNull { it.createdAt }
    }
}