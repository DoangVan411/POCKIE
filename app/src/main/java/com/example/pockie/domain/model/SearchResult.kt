package com.example.pockie.domain.model

data class SearchResult (
    val account: Account,
    val status: FriendStatus
)

enum class FriendStatus {
    FRIEND,
    REQUEST_SENT,
    REQUEST_RECEIVED,
    NONE
}
