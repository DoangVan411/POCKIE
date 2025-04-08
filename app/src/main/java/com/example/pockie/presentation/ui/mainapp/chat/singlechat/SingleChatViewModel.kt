package com.example.pockie.presentation.ui.mainapp.chat.singlechat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.usecase.GetAccountUseCase
import com.example.pockie.domain.usecase.GetAccountsUseCase
import com.example.pockie.domain.usecase.GetMessagesUseCase
import com.example.pockie.domain.usecase.SendMessageUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SingleChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Chat>>(emptyList())
    val messages: StateFlow<List<Chat>> = _messages.asStateFlow()

    private val _account = MutableStateFlow<Account>(Account())
    val account: StateFlow<Account> = _account.asStateFlow()

    fun getCurrentUserUid(): String {
        return auth.currentUser!!.uid
    }

    fun getMessages(chatId: String) {
        viewModelScope.launch {
            getMessagesUseCase(chatId).collect {messages ->
                _messages.value = messages
            }
        }
    }

    fun getChatId(senderId: String, receiverId: String): String {
        return if(senderId < receiverId) "$senderId-$receiverId" else "$receiverId-$senderId"
    }

    fun sendMessage(senderId: String, receiverId: String, content: String, createdAt: Date) {
        val chat = Chat(senderId, receiverId, content, createdAt)
        viewModelScope.launch {
            sendMessageUseCase(chat)
        }
    }

    fun getAccount(uid: String) {
        viewModelScope.launch {
            _account.value = getAccountUseCase(uid)
        }

    }
}