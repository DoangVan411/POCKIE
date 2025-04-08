package com.example.pockie.presentation.ui.mainapp.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.usecase.GetAccountsUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
): ViewModel() {
    private val _usersState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val usersState: StateFlow<NetworkState> = _usersState.asStateFlow()

    fun getAccounts() {
        viewModelScope.launch {
            _usersState.value = NetworkState.Loading
            try {
                getAccountsUseCase().collect{state ->
                    _usersState.value = state
                }
            } catch (e: Exception) {
                _usersState.value = NetworkState.Error(e.message.toString())
            }
        }
    }
}