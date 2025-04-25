package com.example.pockie.domain.usecase

import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AccountRepository
import com.example.pockie.domain.repository.FriendRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


//toi uu sau
class GetFriendsUseCase @Inject constructor(private val friendRepository: FriendRepository){
    operator fun invoke(): Flow<NetworkState>{
        return friendRepository.getFriends()
    }
}