package com.example.pockie.data.source.remote

import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth) {
    suspend fun getAccount(uid: String): Account {
        val snapshot = firestore.collection("accounts").document(uid).get().await()
        return snapshot?.toObject(Account::class.java) ?: Account()
    }

    fun getAllAccounts(): Flow<NetworkState> = callbackFlow {
        val listener = firestore.collection("accounts")
            .addSnapshotListener { snapshot, _ ->
                val accounts = snapshot?.toObjects(Account::class.java) ?: emptyList()
                trySend(NetworkState.Success<List<Account>>(accounts))
            }
        awaitClose { listener.remove() }
    }

    fun editAccount(fullName: String, bio: String, avtUrl: String): Flow<NetworkState> = callbackFlow {
        try{
            val uid = auth.currentUser!!.uid
            val updates = mapOf(
                "fullName" to fullName,
                "bio" to bio,
                "avtUrl" to avtUrl
            )
            firestore.collection("accounts").document(uid).update(updates).await()
            trySend(NetworkState.Success("Edit success!!"))
        }
        catch(e: Exception){
            trySend(NetworkState.Error(e.message.toString()))
        }
        awaitClose {  }
    }
}