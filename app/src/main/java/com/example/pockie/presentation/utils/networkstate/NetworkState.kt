package com.example.pockie.presentation.utils.networkstate

sealed class NetworkState {
    data object Init: NetworkState()
    data object Loading: NetworkState()
    data class Success<T>(val data: T? = null): NetworkState()
    data class Error(val message: String): NetworkState()
}