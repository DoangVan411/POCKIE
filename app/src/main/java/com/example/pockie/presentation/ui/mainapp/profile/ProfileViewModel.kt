package com.example.pockie.presentation.ui.mainapp.profile

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.usecase.GetAccountUseCase
import com.example.pockie.domain.usecase.GetPostUserUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getPostUserUseCase: GetPostUserUseCase,
    private val auth: FirebaseAuth,
): ViewModel() {
    private val _account = MutableStateFlow(Account())
    val account: StateFlow<Account> = _account.asStateFlow()

    private val _posts = MutableStateFlow<NetworkState>(NetworkState.Init)
    val posts: StateFlow<NetworkState> get() = _posts

    fun getAccount() {
        val currentUser = auth.currentUser!!.uid
        viewModelScope.launch {
            _account.value = getAccountUseCase(currentUser)
        }
    }

    fun getPosts(){
        _posts.value = NetworkState.Loading
        viewModelScope.launch {
            getPostUserUseCase.invoke().collect{_posts.value = it}
        }
    }
}