package com.example.pockie.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post (
    val content: String = "",
    val imageUrl: String = "",
    @ServerTimestamp
    val createAt: Date = Date(),
    val userId: String = "",
    val likeCount: Int = 0,
)