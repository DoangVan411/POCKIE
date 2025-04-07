package com.example.pockie.presentation.ui.authentication.password

import androidx.lifecycle.ViewModel
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _signInState =  MutableStateFlow<NetworkState>(NetworkState.Init)
    val signInState: StateFlow<NetworkState> get() = _signInState
    private val _signUpState =  MutableStateFlow<NetworkState>(NetworkState.Init)
    val signUpState: StateFlow<NetworkState> get() = _signUpState

    fun signIn(email: String, passwd: String){
        _signInState.value = NetworkState.Loading
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener { result ->
            if(result.isSuccessful){
                _signInState.value = NetworkState.Success<Unit>()
            }
            else{
                _signInState.value = NetworkState.Error(result.exception?.message ?: "Error Sign In")
            }
        }
    }

    fun signUp(email: String, passwd: String){
        _signUpState.value = NetworkState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener { result ->
            if(result.isSuccessful){
                _signUpState.value = NetworkState.Success<Unit>()
            }
            else{
                _signUpState.value = NetworkState.Error(result.exception?.message ?: "Error Sign Up")
            }
        }
    }
}