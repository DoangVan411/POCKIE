package com.example.pockie.domain.repository

import com.example.pockie.domain.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(chat: Chat): String?
    fun getMessages(chatId: String): Flow<List<Chat>>
}