package com.gumrindelwald.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// A global state to track if the app is running. Both consumed by the UI and the service.
class RunningStatusTracker {
    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    fun startTracking() {
        _isRunning.value = true
    }

    fun stopTracking() {
        _isRunning.value = false
    }
}