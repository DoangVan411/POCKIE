package com.example.pockie.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Chat (
    val senderId: String = "",
    val receiverId: String = "",
    val content: String = "",
    @ServerTimestamp
    val createdAt: Date = Date()
)