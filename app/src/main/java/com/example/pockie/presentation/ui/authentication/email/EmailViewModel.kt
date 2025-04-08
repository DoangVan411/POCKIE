package com.example.pockie.presentation.ui.authentication.email

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.usecases.ResetPasswordUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EmailViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private val _resetPasswordState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val resetPasswordState: Flow<NetworkState> get() = _resetPasswordState

    fun resetPassword(email: String){
        _resetPasswordState.value = NetworkState.Loading
        viewModelScope.launch {
            resetPasswordUseCase.invoke(email).collect{_resetPasswordState.value = it}
        }
    }
}