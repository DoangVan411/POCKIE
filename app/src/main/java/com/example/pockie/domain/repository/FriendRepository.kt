package com.example.pockie.domain.repository

import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    suspend fun sendRequest(senderId: String, receiverId: String)
    suspend fun acceptFriendRequest(senderId: String, receiverId: String)
    fun getSentRequests(): Flow<NetworkState>
    fun getReceivedRequests(): Flow<NetworkState>
    fun getFriends(): Flow<NetworkState>
    suspend fun deleteFriend(friendId: String)
}