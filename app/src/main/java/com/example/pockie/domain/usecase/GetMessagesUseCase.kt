package com.example.pockie.domain.usecase

import com.example.pockie.domain.repository.ChatRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val chatRepository: ChatRepository){
    operator fun invoke(chatId: String): Flow<NetworkState> {
        return chatRepository.getMessages(chatId)
    }
}