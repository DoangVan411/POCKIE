package com.example.pockie.data.repository

import com.example.pockie.data.source.remote.FirebaseChatDataSource
import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val firebaseChatDataSource: FirebaseChatDataSource): ChatRepository {
    override suspend fun sendMessage(chat: Chat): String? {
        val chatId = getChatId(chat.senderId, chat.receiverId)
        return firebaseChatDataSource.sendMessage(chatId, chat)
    }

    override fun getMessages(chatId: String): Flow<List<Chat>> {
        return firebaseChatDataSource.getMessages(chatId)
    }

    private fun getChatId(sender: String, receiver: String): String{
        return if(sender < receiver) "$sender-$receiver" else "$receiver-$sender"
    }

}