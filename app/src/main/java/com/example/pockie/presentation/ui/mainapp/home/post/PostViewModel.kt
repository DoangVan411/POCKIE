package com.example.pockie.presentation.ui.mainapp.home.post

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.usecase.GetAllPostUseCase
import com.example.pockie.domain.usecase.SendMessageUseCase
import com.example.pockie.domain.usecase.UpdatePostUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllPostUseCase: GetAllPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _allPost = MutableStateFlow<NetworkState>(NetworkState.Init)
    val allPost: StateFlow<NetworkState> get() = _allPost

    private val _updatePost = MutableStateFlow<NetworkState>(NetworkState.Init)
    val updatePost: StateFlow<NetworkState> get() = _updatePost

    private val _replyPost = MutableStateFlow<NetworkState>(NetworkState.Init)
    val replyPost: StateFlow<NetworkState> get() = _replyPost

    fun getAllPost(){
        _allPost.value = NetworkState.Loading
        viewModelScope.launch {
            getAllPostUseCase.invoke().collect{_allPost.value = it}
        }
    }

    fun updatePost(post: Post){
        viewModelScope.launch {
            updatePostUseCase.invoke(post = post).collect{_updatePost.value = it}
        }
    }

    fun replyPost(message: String, post: Post){
        _replyPost.value = NetworkState.Loading
        viewModelScope.launch {
            sendMessageUseCase.invoke(Chat(auth.currentUser!!.uid, post.userId, message, Date()))
        }
    }
}