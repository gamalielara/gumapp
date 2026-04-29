package com.gumrindelwald.presentation

sealed interface ActiveRunEventChannel {
    data class Error(val error: UIText) : ActiveRunEventChannel
    data object RunSaved : ActiveRunEventChannel
}