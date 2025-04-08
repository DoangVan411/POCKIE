package com.example.pockie.data.source.remote

import com.example.pockie.domain.model.Chat
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

        val snapshot = firestore.collection("users")
            .document(chat.receiverId)
            .get().await()

        val fcmToken = snapshot.getString("fcmToken")
        return fcmToken
    }

    fun getMessages(chatId: String): Flow<List<Chat>> = callbackFlow {
        val listener = firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("createdAt")
            .addSnapshotListener {snapshot, _ ->
                val messages = snapshot?.toObjects(Chat::class.java) ?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }
}