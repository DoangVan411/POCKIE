package com.example.pockie.domain.model

data class Account(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val fcmToken: String = ""
)