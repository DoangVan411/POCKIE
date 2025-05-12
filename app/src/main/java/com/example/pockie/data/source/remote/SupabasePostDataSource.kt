package com.example.pockie.data.source.remote

import com.example.pockie.presentation.utils.networkstate.NetworkState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class SupabasePostDataSource @Inject constructor(
    private val storage: Storage
) {
    fun generateLinkPhoto(fileName: String, photoFile: File): Flow<NetworkState> = callbackFlow{
        trySend(NetworkState.Loading)
        try {
            storage.from("post-picture").upload(path = fileName, data = photoFile.readBytes())
            val publicUrl = storage.from("post-picture").publicUrl(fileName)

            trySend(NetworkState.Success<String>(publicUrl))
        }
        catch (e: Exception){
            trySend(NetworkState.Error(e.message.toString()))
        }
        awaitClose{}
    }

}