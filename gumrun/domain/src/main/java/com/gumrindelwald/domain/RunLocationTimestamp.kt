package com.gumrindelwald.domain

import kotlin.time.Duration

data class RunLocationTimestamp(
    val location: RunLocation, val duration: Duration
)
