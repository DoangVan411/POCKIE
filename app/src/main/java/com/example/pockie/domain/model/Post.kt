package com.example.pockie.domain.model

import java.util.Date

data class Post (
    val content: String = "",
    val imageUrl: Int = 0,
    val createAt: Date = Date(),
    val likeCount: Int = 0,
)