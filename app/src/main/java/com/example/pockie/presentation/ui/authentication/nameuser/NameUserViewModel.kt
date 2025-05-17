package com.example.pockie.presentation.ui.authentication.nameuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.usecase.SaveAccountUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameUserViewModel @Inject constructor(
    private val saveAccountUseCase: SaveAccountUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _saveDataState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val saveDataState: StateFlow<NetworkState> get() = _saveDataState

    fun saveUserToFireStore(email: String, password: String, name: String) {
        _saveDataState.value = NetworkState.Loading
        viewModelScope.launch {
            saveAccountUseCase.invoke(
                Account(
                    firebaseAuth.currentUser?.uid ?: "null",
                    name,
                    email,
                    password,
                    avtUrl = "https://icon-library.com/images/anonymous-avatar-icon/anonymous-avatar-icon-25.jpg"
                )
            ).collect{_saveDataState.value = it}
        }
    }
}