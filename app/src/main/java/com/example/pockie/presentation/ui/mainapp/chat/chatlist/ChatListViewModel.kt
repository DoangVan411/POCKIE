package com.example.pockie.presentation.ui.mainapp.chat.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.usecase.GetAccountsUseCase
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
    private val _userList = MutableStateFlow<List<Account>>(emptyList())
    val userList: StateFlow<List<Account>> = _userList.asStateFlow()

    fun getAccounts() {
        viewModelScope.launch {
            getAccountsUseCase().collect{userList ->
                _userList.value = userList
            }
        }
    }
}