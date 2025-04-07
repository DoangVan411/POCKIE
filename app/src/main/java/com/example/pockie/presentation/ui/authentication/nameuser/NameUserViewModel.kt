package com.example.pockie.presentation.ui.authentication.nameuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class NameUserViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _saveDataState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val saveDataState: StateFlow<NetworkState> get() = _saveDataState

    fun saveUserToFireStore(email: String, password: String, name: String){
        _saveDataState.value = NetworkState.Loading
        viewModelScope.launch {
            try {
                val uid = firebaseAuth.currentUser?.uid ?: "null"
                val account = Account(
                    uid, name,email, password
                )
                firestore.collection("accounts").document(uid).set(account).await()
                _saveDataState.value = NetworkState.Success<Unit>()
            }
            catch (e: Exception){
                _saveDataState.value = NetworkState.Error(e.message ?: "Error save data.")
            }
        }
    }
}