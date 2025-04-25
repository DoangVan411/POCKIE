package com.example.pockie.data.repository

import com.example.pockie.data.source.remote.FirebaseFriendDataSource
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.FriendRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(private val firebaseFriendDataSource: FirebaseFriendDataSource): FriendRepository{
    override suspend fun sendRequest(senderId: String, receiverId: String) {
        return firebaseFriendDataSource.sendFriendRequest(senderId, receiverId)
    }

    override suspend fun acceptFriendRequest(senderId: String, receiverId: String) {
        return firebaseFriendDataSource.acceptFriendRequest(senderId, receiverId)
    }

    override fun getSentRequests(): Flow<NetworkState> {
        return firebaseFriendDataSource.getSentRequests()
    }

    override fun getReceivedRequests(): Flow<NetworkState> {
        return firebaseFriendDataSource.getReceivedRequests()
    }

    override fun getFriends(): Flow<NetworkState> {
        return firebaseFriendDataSource.getFriends()
    }

    override suspend fun deleteFriend(friendId: String) {
        return firebaseFriendDataSource.deleteFriend(friendId)
    }
}