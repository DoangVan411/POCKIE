package com.example.pockie.data.repository

import android.util.Log
import com.example.pockie.data.source.remote.FirebasePostDataSource
import com.example.pockie.data.source.remote.SupabasePostDataSource
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.repository.PostRepository
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val supabasePostDataSource: SupabasePostDataSource,
    private val firebasePostDataSource: FirebasePostDataSource,
): PostRepository {
    override fun generateLinkPhoto(fileName: String, filePhoto: File): Flow<NetworkState> {
        return supabasePostDataSource.generateLinkPhoto(fileName, filePhoto)
    }

    override fun uploadPhoto(post: Post): Flow<NetworkState> {
        return firebasePostDataSource.uploadPost(post)
    }

    override fun getAllPost(tagId: Int): Flow<NetworkState> {
        return firebasePostDataSource.getAllPost(tagId)
    }

    override fun updatePost(post: Post): Flow<NetworkState> {
        return firebasePostDataSource.updatePost(post)
    }

    override fun getPostUser(uid: String): Flow<NetworkState> {
        return firebasePostDataSource.getPostsUser(uid)
    }

    override suspend fun deletePost(post: Post){
        return firebasePostDataSource.deletePost(post)
    }
}