package com.gumrindelwald.presentation.screens.active_run

import com.gumrindelwald.presentation.UIText

sealed interface ActiveRunEventChannel {
    data class Error(val error: UIText) : ActiveRunEventChannel
    data object RunSaved : ActiveRunEventChannel
}