package com.example.pockie.data.dto


data class FCMRequest (
    val message: Message
)

data class Message(
    val token: String,
    val notification: Notification
)

data class Notification (
    val title: String,
    val body: String
)