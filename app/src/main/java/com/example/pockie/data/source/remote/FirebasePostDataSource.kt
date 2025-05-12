package com.example.pockie.data.source.remote

import android.util.Log
import com.example.pockie.domain.model.Post
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePostDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun uploadPost(post: Post): Flow<NetworkState> = callbackFlow {
        trySend(NetworkState.Loading)
        try {
            firestore.collection("posts").add(post.copy(userId = auth.currentUser?.uid ?: "anonymous"))
                .await()
            trySend(NetworkState.Success<Unit>())
        } catch (e: Exception) {
            trySend(NetworkState.Error(e.message.toString()))
        }

        awaitClose { }
    }

    fun getAllPost(): Flow<NetworkState> = callbackFlow {
        trySend(NetworkState.Loading)
        try {
            val listener = firestore.collection("posts")
                .orderBy("createAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot,_ ->
                    val posts = snapshot?.toObjects(Post::class.java) ?: emptyList()

                    trySend(NetworkState.Success(posts))
                }

            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(NetworkState.Error(e.message.toString()))
        }
    }
}