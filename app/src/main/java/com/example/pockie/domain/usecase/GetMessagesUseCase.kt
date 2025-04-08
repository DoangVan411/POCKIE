package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val chatRepository: ChatRepository){
    operator fun invoke(chatId: String): Flow<List<Chat>> {
        return chatRepository.getMessages(chatId)
    }
}