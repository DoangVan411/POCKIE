package com.example.pockie.domain.repository

import com.example.pockie.domain.model.Chat
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(chat: Chat): String?
    fun getMessages(chatId: String): Flow<NetworkState>
    suspend fun getMessagesOnce(chatId: String): List<Chat>
}