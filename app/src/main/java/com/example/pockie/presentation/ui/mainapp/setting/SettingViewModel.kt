package com.example.pockie.presentation.ui.mainapp.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.usecase.DeleteAccountUseCase
import com.example.pockie.domain.usecase.GetAccountUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getAccountUseCase: GetAccountUseCase,
): ViewModel() {

    private val _deleteAccountState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val deleteAccountState: StateFlow<NetworkState> = _deleteAccountState.asStateFlow()

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _deleteAccountState.value = NetworkState.Loading
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val account = getAccountUseCase(currentUser.uid)
                    val result = deleteAccountUseCase(account)
                    _deleteAccountState.value = result
                } else {
                    _deleteAccountState.value = NetworkState.Error("User not found")
                }
            } catch (e: Exception) {
                _deleteAccountState.value = NetworkState.Error(e.message ?: "Unknown error")
            }
        }
    }
}