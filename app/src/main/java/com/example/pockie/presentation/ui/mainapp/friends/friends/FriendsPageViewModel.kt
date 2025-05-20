package com.example.pockie.presentation.ui.friends.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.usecase.DeleteFriendUseCase
import com.example.pockie.domain.usecase.GetFriendsUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsPageViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val deleteFriendUseCase: DeleteFriendUseCase,
) : ViewModel() {

    private val _friendsState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val friendsState: StateFlow<NetworkState> = _friendsState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var allFriends: List<Account> = emptyList()

    init {
        observeSearchQuery()
        fetchFriends()
    }

    private fun fetchFriends() {
        viewModelScope.launch {
            _friendsState.value = NetworkState.Loading
            try {
                getFriendsUseCase().collect { state ->
                    when (state) {
                        is NetworkState.Success<*> -> {
                            allFriends = state.data as? List<Account> ?: emptyList()
                            _friendsState.value = NetworkState.Success(allFriends)
                        }
                        is NetworkState.Error -> _friendsState.value = state
                        is NetworkState.Loading -> _friendsState.value = NetworkState.Loading
                        is NetworkState.Init -> _friendsState.value = NetworkState.Init
                    }
                }
            } catch (e: Exception) {
                _friendsState.value = NetworkState.Error(e.message.orEmpty())
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    filterFriends(query)
                }
        }
    }

    private fun filterFriends(query: String) {
        val filteredList = if (query.isBlank()) {
            allFriends
        } else {
            allFriends.filter {
                it.fullName.contains(query, ignoreCase = true)
            }
        }
        _friendsState.value = NetworkState.Success(filteredList)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun deleteFriend(friendId: String) {
        viewModelScope.launch {
            deleteFriendUseCase(friendId)
            // Optional: update local list after delete
            allFriends = allFriends.filter { it.uid != friendId }
            filterFriends(_searchQuery.value)
        }
    }
}
