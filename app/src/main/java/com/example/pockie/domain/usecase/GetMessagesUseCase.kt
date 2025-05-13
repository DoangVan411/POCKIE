package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.ChatRepository
import com.example.pockie.presentation.utils.Utils.getChatId
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
){
    suspend operator fun invoke(friendId: String): Flow<NetworkState> {
        val chatId = getCurrentUserIdUseCase()?.let { getChatId(it, friendId) }
        return chatRepository.getMessages(chatId!!)
    }
}