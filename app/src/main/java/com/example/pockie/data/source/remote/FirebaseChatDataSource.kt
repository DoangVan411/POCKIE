package com.example.pockie.data.source.remote

import android.util.Log
import com.example.pockie.domain.model.Chat
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseChatDataSource @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun sendMessage(chatId: String, chat: Chat): String? {
        firestore.collection("chats").document(chatId).collection("messages")
            .add(chat).await()

        val snapshot = firestore.collection("accounts")
            .document(chat.receiverId)
            .get().await()


        val fcmToken = snapshot.getString("fcmToken")
        return fcmToken
    }

    fun getMessages(chatId: String): Flow<NetworkState> = callbackFlow {
        val listener = firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("createdAt")
            .addSnapshotListener {snapshot, _ ->
                val messages = snapshot?.toObjects(Chat::class.java) ?: emptyList()
                Log.d("Messages Firebase", "${messages.size}")
                trySend(NetworkState.Success<List<Chat>>(messages))
            }
        awaitClose { listener.remove() }
    }

    suspend fun getMessagesOnce(chatId: String): List<Chat> {
        val snapshot = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .get()
            .await()
        return snapshot.toObjects(Chat::class.java)
    }
}