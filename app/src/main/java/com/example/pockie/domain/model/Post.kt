package com.example.pockie.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post(
    val id: String = "",
    val content: String = "",
    val imageUrl: String = "",
    @ServerTimestamp
    val createAt: Date = Date(),
    val userId: String = "",
    val likedBy: MutableList<String> = mutableListOf(),
)

data class PostItem(
    val post: Post,
    val fullname: String,
    val avtUrl: String,
)