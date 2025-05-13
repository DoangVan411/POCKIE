package com.example.pockie.presentation.ui.mainapp.chat

import com.example.pockie.domain.model.Account

data class ChatListItem (
    val account: Account,
    val lastMessage: String?,
    val lastMessageTimestamp: String?
)