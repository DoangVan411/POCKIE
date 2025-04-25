package com.example.pockie.data.source.remote

import android.util.Log
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFriendDataSource @Inject constructor(private val firestore: FirebaseFirestore, private val firebaseAuth: FirebaseAuth, private val firebaseUserDataSource: FirebaseUserDataSource) {
    suspend fun sendFriendRequest(senderId: String, receiverId: String){
        val senderAccount = firebaseUserDataSource.getAccount(senderId)
        val receiverAccount = firebaseUserDataSource.getAccount(receiverId)

        val senderRef = firestore.collection("users").document(senderId)
            .collection("sentRequests").document(receiverId)

        val receiverRef = firestore.collection("users").document(receiverId)
            .collection("receivedRequests").document(senderId)


        firestore.runBatch { batch ->
            batch.set(senderRef, receiverAccount)
            batch.set(receiverRef, senderAccount)
        }.await()
    }

    suspend fun acceptFriendRequest(senderId: String, receiverId: String) {
        val senderRef = firestore.collection("users").document(senderId)
            .collection("sentRequests").document(receiverId)

        val receiverRef = firestore.collection("users").document(receiverId)
            .collection("receivedRequests").document(senderId)

        val senderFriendsRef = firestore.collection("users").document(senderId)
            .collection("friends").document(receiverId)

        val receiverFriendsRef = firestore.collection("users").document(receiverId)
            .collection("friends").document(senderId)

        val senderAccount = firebaseUserDataSource.getAccount(senderId)
        val receiverAccount = firebaseUserDataSource.getAccount(receiverId)


        firestore.runBatch { batch ->
            batch.delete(senderRef)
            batch.delete(receiverRef)

            batch.set(senderFriendsRef, receiverAccount)
            batch.set(receiverFriendsRef, senderAccount)
        }.await()
    }

    fun getFriends(): Flow<NetworkState> = callbackFlow {
        val ref = firestore.collection("users").document(firebaseAuth.currentUser?.uid ?: "")
            .collection("friends")

        val listener = ref.addSnapshotListener { snapshot, _ ->
            val accounts = snapshot?.documents?.mapNotNull { it.toObject(Account::class.java) } ?: emptyList()
            trySend(NetworkState.Success<List<Account>>(accounts))
        }

        awaitClose { listener.remove() }
    }

    fun getSentRequests(): Flow<NetworkState> = callbackFlow {
        val ref = firestore.collection("users").document(firebaseAuth.currentUser?.uid ?: "")
            .collection("sentRequests")

        val listener = ref.addSnapshotListener { snapshot, _ ->
            val accounts = snapshot?.documents?.mapNotNull { it.toObject(Account::class.java) } ?: emptyList()
            trySend(NetworkState.Success<List<Account>>(accounts))
        }

        awaitClose { listener.remove() }
    }

    fun getReceivedRequests(): Flow<NetworkState> = callbackFlow {
        val ref = firestore.collection("users").document(firebaseAuth.currentUser?.uid ?: "")
            .collection("receivedRequests")

        val listener = ref.addSnapshotListener { snapshot, _ ->
            val accounts = snapshot?.documents?.mapNotNull { it.toObject(Account::class.java) } ?: emptyList()
            trySend(NetworkState.Success<List<Account>>(accounts))
        }

        awaitClose { listener.remove() }
    }

    suspend fun deleteFriend(friendId: String) {
        val userRef = firestore.collection("users").document(firebaseAuth.currentUser?.uid ?: return)
            .collection("friends").document(friendId)
        val friendRef = firestore.collection("users").document(friendId).collection("friends")
            .document(firebaseAuth.currentUser?.uid ?: return)

        firestore.runBatch {batch ->
            batch.delete(userRef)
            batch.delete(friendRef)
        }.await()
    }
}