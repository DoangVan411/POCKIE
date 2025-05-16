package com.example.pockie.data.repository

import android.util.Log
import com.example.pockie.data.dto.FCMRequest
import com.example.pockie.data.dto.FCMResponse
import com.example.pockie.data.source.remote.APIService
import com.example.pockie.domain.repository.NotificationRepository
import retrofit2.Response

class NotificationRepositoryImpl(private val apiService: APIService): NotificationRepository {
    override suspend fun pushNotification(
        accessToken: String,
        request: FCMRequest
    ): Response<FCMResponse> {
        return try {
            val response = apiService.pushNotification("Bearer $accessToken", request)
            Log.d("NotificationRepositoryImpl", "pushNotification: ${response.body()}")
            response
        } catch (e: Exception) {
            Response.error(500, okhttp3.ResponseBody.create(null, "Exception: ${e.message}"))
        }
    }
}