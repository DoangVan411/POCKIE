package com.example.pockie.presentation.ui.friends.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.usecase.DeleteFriendUseCase
import com.example.pockie.domain.usecase.GetFriendsUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsPageViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val deleteFriendUseCase: DeleteFriendUseCase
): ViewModel() {
    private val _friendsState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val friendsState: StateFlow<NetworkState> = _friendsState.asStateFlow()

    fun getFriends() {
        viewModelScope.launch {
            _friendsState.value = NetworkState.Loading
            try {
                getFriendsUseCase().collect {state ->
                    _friendsState.value = state
                }
            } catch (e: Exception) {
                _friendsState.value = NetworkState.Error(e.message.toString())
            }
        }
    }

    fun deleteFriend(friendId: String) {
        viewModelScope.launch {
            deleteFriendUseCase(friendId)
        }
    }
}