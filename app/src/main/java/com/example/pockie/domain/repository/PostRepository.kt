package com.example.pockie.domain.repository

import com.example.pockie.domain.model.Post
import com.example.pockie.presentation.utils.networkstate.NetworkState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PostRepository {
    fun generateLinkPhoto(fileName: String, filePhoto: File): Flow<NetworkState>
    fun uploadPhoto(post: Post): Flow<NetworkState>
    fun getAllPost(): Flow<NetworkState>
    fun updatePost(post: Post): Flow<NetworkState>
    fun getPostUser(): Flow<NetworkState>
}