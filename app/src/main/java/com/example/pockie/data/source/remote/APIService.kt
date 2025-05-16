package com.example.pockie.data.source.remote

import com.example.pockie.data.dto.FCMRequest
import com.example.pockie.data.dto.FCMResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers("Content-Type:application/json")
    @POST("/v1/projects/pockie-d50fb/messages:send")
    suspend fun pushNotification(
        @Header("Authorization") accessToken: String,
        @Body request: FCMRequest
    ): Response<FCMResponse>
}
