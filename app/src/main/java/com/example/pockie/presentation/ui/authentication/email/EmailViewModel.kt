package com.example.pockie.presentation.ui.authentication.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.usecase.ResetPasswordUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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