package com.example.pockie.presentation.ui.mainapp.friends.requests


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.model.FriendStatus
import com.example.pockie.domain.model.SearchResult
import com.example.pockie.domain.usecase.AcceptFriendRequestUseCase
import com.example.pockie.domain.usecase.GetCurrentUserIdUseCase
import com.example.pockie.domain.usecase.GetFriendsUseCase
import com.example.pockie.domain.usecase.GetReceivedRequestsUseCase
import com.example.pockie.domain.usecase.GetSentRequestsUseCase
import com.example.pockie.domain.usecase.SearchUserUseCase
import com.example.pockie.domain.usecase.SendRequestUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RequestPageViewModel @Inject constructor(
    private val searchUserUseCase: SearchUserUseCase,
    private val sendRequestUseCase: SendRequestUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val getSentRequestsUseCase: GetSentRequestsUseCase,
    private val getReceivedRequestsUseCase: GetReceivedRequestsUseCase,
    private val getFriendsUseCase: GetFriendsUseCase
): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResultsWithStatus = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResultsWithStatus: StateFlow<List<SearchResult>> = _searchResultsWithStatus.asStateFlow()

    private val searchResults: StateFlow<List<Account>?> = searchUserUseCase(searchQuery)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _receivedRequestsState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val receivedRequestsState: StateFlow<NetworkState> = _receivedRequestsState.asStateFlow()

    private val _sentRequestsState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val sentRequestsState: StateFlow<NetworkState> = _sentRequestsState.asStateFlow()

    private val _friendsState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val friendsState: StateFlow<NetworkState> = _friendsState.asStateFlow()

    private val sentRequestIds = MutableStateFlow<Set<String>>(emptySet())
    private val receivedRequestIds = MutableStateFlow<Set<String>>(emptySet())
    private val friendIds = MutableStateFlow<Set<String>>(emptySet())

    init {
        viewModelScope.launch {
            combine(
                searchResults,
                sentRequestIds,
                receivedRequestIds,
                friendIds
            ) { _, _, _, _ ->
                updateSearchResultsWithStatus()
            }.collect {updateSearchResultsWithStatus()}
        }
    }

    private fun updateSearchResultsWithStatus() {
        val results = searchResults.value ?: return
        val sent = sentRequestIds.value
        val received = receivedRequestIds.value
        val friends = friendIds.value

        _searchResultsWithStatus.value = results.map { account ->
            val status = when (account.uid) {
                in friends -> FriendStatus.FRIEND
                in sent -> FriendStatus.REQUEST_SENT
                in received -> FriendStatus.REQUEST_RECEIVED
                else -> FriendStatus.NONE
            }
            SearchResult(account, status)
        }
    }

    fun getReceivedRequests() {
        viewModelScope.launch {
            _receivedRequestsState.value = NetworkState.Loading
            try {
                getReceivedRequestsUseCase().collect {state ->
                    _receivedRequestsState.value = state
                    if(state is NetworkState.Success<*>) {
                        val list = state.data as List<Account>
                        val friendIds = list.map { it.uid }.toSet()
                        this@RequestPageViewModel.receivedRequestIds.value = friendIds
                    }
                }
            } catch (e: Exception) {
                _receivedRequestsState.value = NetworkState.Error(e.message.toString())
            }
        }
    }

    fun getFriends() {
        viewModelScope.launch {
            _friendsState.value = NetworkState.Loading
            try {
                getFriendsUseCase().collect {state ->
                    _friendsState.value = state
                    if(state is NetworkState.Success<*>) {
                        val list = state.data as List<Account>
                        val friendIds = list.map { it.uid }.toSet()
                        this@RequestPageViewModel.friendIds.value = friendIds
                    }
                }
            } catch (e: Exception) {
                _friendsState.value = NetworkState.Error(e.message.toString())
            }
        }
    }

    fun getSentRequests() {
        viewModelScope.launch {
            _sentRequestsState.value = NetworkState.Loading
            try {
                getSentRequestsUseCase().collect { state ->
                    _sentRequestsState.value = state
                    if(state is NetworkState.Success<*>) {
                        val list = state.data as List<Account>
                        val friendIds = list.map { it.uid }.toSet()
                        this@RequestPageViewModel.sentRequestIds.value = friendIds
                    }
                }
            } catch (e: Exception) {
                _sentRequestsState.value = NetworkState.Error(e.message.toString())
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun sendRequest(receiverId: String) {
        viewModelScope.launch {
            val senderId = getCurrentUserIdUseCase()
            if (senderId != null) {
                sendRequestUseCase(senderId, receiverId)
            }
        }
    }

    fun acceptRequest(senderId: String) {
        viewModelScope.launch {
            val receiverId = getCurrentUserIdUseCase()
            if (receiverId != null) {
                acceptFriendRequestUseCase(senderId, receiverId)
            }
        }
    }

}