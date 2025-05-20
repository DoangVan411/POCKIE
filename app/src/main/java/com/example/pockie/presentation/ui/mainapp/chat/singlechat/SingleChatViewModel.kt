package com.example.pockie.presentation.ui.mainapp.chat.singlechat

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.data.source.remote.AccessToken
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.usecase.GetAccountUseCase
import com.example.pockie.domain.usecase.GetCurrentUserIdUseCase
import com.example.pockie.domain.usecase.GetMessagesUseCase
import com.example.pockie.domain.usecase.PushNotificationUseCase
import com.example.pockie.domain.usecase.SendMessageUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
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
    private val auth: FirebaseAuth,
    private val pushNotificationUseCase: PushNotificationUseCase
) : ViewModel() {

    private val _messageState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val messageState: StateFlow<NetworkState> = _messageState.asStateFlow()

    private val _account = MutableStateFlow<Account>(Account())
    val account: StateFlow<Account> = _account.asStateFlow()

    fun getCurrentUserUid(): String {
        return auth.currentUser!!.uid
    }

    fun getMessages(chatId: String) {
        viewModelScope.launch {
            _messageState.value = NetworkState.Loading
            try {
                getMessagesUseCase(chatId).collect {state ->
                    _messageState.value = state
                }
            }
            catch (e: Exception) {
                _messageState.value = NetworkState.Error(e.message.toString())
            }
        }
    }


    fun sendMessage(senderId: String, receiverId: String, content: String, createdAt: Date, context: Context) {
        val chat = Chat(senderId = senderId, receiverId = receiverId, content = content, createdAt = createdAt)
        viewModelScope.launch {
            val fcmToken = sendMessageUseCase(chat)

            if(fcmToken != null) {
                pushNoti(fcmToken, account.value.fullName, content, context)
            }
        }
    }

    fun getAccount(uid: String) {
        viewModelScope.launch {
            _account.value = getAccountUseCase(uid)
        }

    }



    private fun pushNoti(token: String, title: String, body: String, context: Context) {
        viewModelScope.launch {
            val accessToken = AccessToken.getAccessToken(context)
            pushNotificationUseCase(accessToken!!, token, title, body)
        }
    }
}