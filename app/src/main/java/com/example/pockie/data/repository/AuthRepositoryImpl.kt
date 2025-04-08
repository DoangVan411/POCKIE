package com.example.pockie.data.repository

import com.example.pockie.domain.model.Account
import com.example.pockie.domain.repository.AuthRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {
    override fun register(email: String, password: String): Flow<NetworkState> = callbackFlow{
        trySend(NetworkState.Loading)
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if(result.isSuccessful){
                trySend(NetworkState.Success<Unit>())
            }
            else{
                trySend(NetworkState.Error(result.exception?.message ?: "Error register"))
            }
        }
        awaitClose {  }
    }

    override fun login(email: String, passwd: String): Flow<NetworkState> = callbackFlow {
        trySend(NetworkState.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener { result ->
            if(result.isSuccessful){
                trySend(NetworkState.Success<Unit>())
            }
            else{
                trySend(NetworkState.Error(result.exception?.message ?: "Error login"))
            }
        }
        awaitClose {  }
    }

    override fun saveAccountToFireStore(account: Account): Flow<NetworkState> = callbackFlow{
        trySend(NetworkState.Loading)
        try {
            val uid = firebaseAuth.currentUser?.uid ?: "null"
            firestore.collection("accounts").document(uid).set(account).await()
            trySend(NetworkState.Success<Unit>())
        }
        catch (e: Exception){
            trySend(NetworkState.Error(e.message.toString()))
        }
        awaitClose {  }
    }

    override fun resetPassword(email: String): Flow<NetworkState> = callbackFlow{
        trySend(NetworkState.Loading)
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {result ->
                if(result.isSuccessful){
                    trySend(NetworkState.Success<String>("A link has just been sent to your email. Please check and retrieve your password."))
                }
                else{
                    trySend(NetworkState.Error(result.exception?.message ?: "Failed to reset password"))
                }
            }

        awaitClose{}
    }
}