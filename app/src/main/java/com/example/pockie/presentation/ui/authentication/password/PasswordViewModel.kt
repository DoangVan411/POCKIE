package com.example.pockie.presentation.ui.authentication.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.usecase.LoginUseCase
import com.example.pockie.domain.usecase.RegisterUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _signInState =  MutableStateFlow<NetworkState>(NetworkState.Init)
    val signInState: StateFlow<NetworkState> get() = _signInState
    private val _signUpState =  MutableStateFlow<NetworkState>(NetworkState.Init)
    val signUpState: StateFlow<NetworkState> get() = _signUpState

    fun signIn(email: String, passwd: String){
        _signInState.value = NetworkState.Loading
        viewModelScope.launch {
            loginUseCase.invoke(email, passwd).collect{_signInState.value = it}
        }
    }

    fun signUp(email: String, passwd: String){
        _signUpState.value = NetworkState.Loading
        viewModelScope.launch {
            registerUseCase.invoke(email, passwd).collect{_signUpState.value = it}
        }
    }
}