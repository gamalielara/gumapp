package com.gumrindelwald.domain

import kotlin.time.Duration

data class RunData(
    val distanceMeters: Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations: List<List<RunLocationTimestamp>> = emptyList()
)
