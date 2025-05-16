package com.example.pockie.domain.usecase

import com.example.pockie.data.dto.FCMRequest
import com.example.pockie.data.dto.Message
import com.example.pockie.data.dto.Notification
import com.example.pockie.domain.repository.NotificationRepository
import javax.inject.Inject

class PushNotificationUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {
    suspend operator fun invoke(accessToken: String, token: String, title: String, body: String) {
        val request = FCMRequest(
            message = Message(
                token = token,
                notification = Notification(
                    title = title,
                    body = body
                )
            )
        )
        notificationRepository.pushNotification(accessToken, request)
    }
}