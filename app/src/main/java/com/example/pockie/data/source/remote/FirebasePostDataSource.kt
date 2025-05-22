package com.example.pockie.data.source.remote

import android.util.Log
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.model.PostItem
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePostDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun uploadPost(post: Post): Flow<NetworkState> = callbackFlow {
        trySend(NetworkState.Loading)
        try {
            val docRef = firestore.collection("posts").document()
            docRef.set(post.copy(id = docRef.id, userId = auth.currentUser?.uid ?: "anonymous"))
                .await()
            trySend(NetworkState.Success<Unit>())
        } catch (e: Exception) {
            trySend(NetworkState.Error(e.message.toString()))
        }

        awaitClose { }
    }

    fun getAllPost(tagId: Int): Flow<NetworkState> = callbackFlow {
        trySend(NetworkState.Loading)
        try {
            val currentId = auth.currentUser?.uid.toString()
            val friendRef =
                firestore.collection("users").document(currentId).collection("friends").get()
                    .await()
            val friends = friendRef.documents.map { it.id }.toMutableList()
            friends.add(currentId)

            firestore.collection("posts").orderBy("createAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    val posts = snapshot?.toObjects(Post::class.java)?.filter { it.userId in friends && (tagId == 0 || it.tag.id == tagId) } ?: emptyList()

                    launch {
                        try {
                            val postItems = posts.map { post ->
                                val users =
                                    firestore.collection("accounts").document(post.userId).get()
                                        .await()
                                val fullName = users.data?.get("fullName").toString()
                                val avtUrl = users.data?.get("avtUrl").toString()

                                Log.d("Post", avtUrl)
                                PostItem(post = post, fullname = fullName, avtUrl = avtUrl)
                            }

                            trySend(NetworkState.Success(postItems))
                        } catch (e: Exception) {
                            trySend(NetworkState.Error(e.message.toString()))
                        }
                    }
                }
        } catch (e: Exception) {
            Log.d("Post", e.message.toString())
            trySend(NetworkState.Error(e.message.toString()))
        }
        awaitClose { }
    }

    fun updatePost(post: Post): Flow<NetworkState> = callbackFlow {
        try {
            firestore.collection("posts").document(post.id).update("likedBy", post.likedBy).await()
            trySend(NetworkState.Success<Unit>())
        } catch (e: Exception) {
            trySend(NetworkState.Error(e.message.toString()))
        }
        awaitClose { }
    }

    fun getPostsUser(uid: String): Flow<NetworkState> = callbackFlow {
        trySend(NetworkState.Loading)
        try {
            var userId = if(uid.isNullOrEmpty()) auth.currentUser?.uid.toString() else uid
            firestore.collection("posts").orderBy("createAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    val posts =
                        snapshot?.toObjects(Post::class.java)?.filter { it.userId == userId }
                            ?: emptyList()

                    trySend(NetworkState.Success(posts))
                }
        } catch (e: Exception) {
            trySend(NetworkState.Error(e.message.toString()))
        }
        awaitClose { }
    }

    fun deletePost(post: Post){
        Log.d("Post", "${post.id}")
        try {
            firestore.collection("posts").document(post.id).delete().addOnSuccessListener {
                Log.d("Post", "delete")
            }.addOnFailureListener {
                Log.d("Post", "Failed")
                Log.d("Post", it.message.toString())
            }

        } catch (e: Exception) {
            Log.d("Post", e.message.toString())
        }
    }
}