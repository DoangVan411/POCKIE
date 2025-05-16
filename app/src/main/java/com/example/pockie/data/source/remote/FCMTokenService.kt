package com.example.pockie.data.source.remote

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object FCMTokenService {
    suspend fun getFCMToken(): String {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch(e: Exception) {
            ""
        }
    }
}