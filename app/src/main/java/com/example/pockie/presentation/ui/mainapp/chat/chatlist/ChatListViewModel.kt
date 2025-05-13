package com.example.pockie.presentation.ui.mainapp.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.usecase.GetAccountsUseCase
import com.example.pockie.domain.usecase.GetCurrentUserIdUseCase
import com.example.pockie.domain.usecase.GetFriendsUseCase
import com.example.pockie.domain.usecase.GetMessagesOnceUseCase
import com.example.pockie.domain.usecase.GetMessagesUseCase
import com.example.pockie.presentation.ui.mainapp.chat.ChatListItem
import com.example.pockie.presentation.utils.Utils.getTime
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getMessagesOnceUseCase: GetMessagesOnceUseCase
): ViewModel() {
    private val _usersState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val usersState: StateFlow<NetworkState> = _usersState.asStateFlow()

    fun getAccounts() {
        viewModelScope.launch {
            _usersState.value = NetworkState.Loading
            try {
                getFriendsUseCase().collect{state ->
                    if(state is NetworkState.Success<*>) {
                        val accounts = state.data as? List<Account> ?: emptyList()
                        val items = accounts.map { friend ->
                                async {
                                    val messagesState = getMessagesOnceUseCase(friend.uid)
                                    ChatListItem(
                                        friend,
                                        messagesState?.content ?: "",
                                        getTime(messagesState?.createdAt ?: Date())
                                    )
                                }
                            }.map { it.await() }
                        _usersState.value = NetworkState.Success<List<ChatListItem>>(items)
                    }
                }
            } catch (e: Exception) {
                _usersState.value = NetworkState.Error(e.message.toString())
            }
        }
    }
}