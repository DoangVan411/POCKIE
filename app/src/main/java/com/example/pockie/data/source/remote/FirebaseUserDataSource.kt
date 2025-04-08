package com.example.pockie.data.source.remote

import com.example.pockie.domain.model.Account
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun getAccount(uid: String): Account {
        val snapshot = firestore.collection("accounts").document(uid).get().await()
        return snapshot?.toObject(Account::class.java) ?: Account()
    }

    fun getAccounts(): Flow<List<Account>> = callbackFlow {
        val listener = firestore.collection("accounts")
            .addSnapshotListener { snapshot, _ ->
                val accounts = snapshot?.toObjects(Account::class.java) ?: emptyList()
                trySend(accounts)
            }
        awaitClose { listener.remove() }
    }
}