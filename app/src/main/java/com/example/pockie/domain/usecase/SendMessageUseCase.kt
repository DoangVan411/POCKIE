package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(chat: Chat): String? {
        return chatRepository.sendMessage(chat)
    }
}