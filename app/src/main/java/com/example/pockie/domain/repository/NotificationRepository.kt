package com.example.pockie.domain.repository

import com.example.pockie.data.dto.FCMRequest
import com.example.pockie.data.dto.FCMResponse
import retrofit2.Response

interface NotificationRepository {
    suspend fun pushNotification(
        accessToken: String,
        request: FCMRequest
    ): Response<FCMResponse>
}