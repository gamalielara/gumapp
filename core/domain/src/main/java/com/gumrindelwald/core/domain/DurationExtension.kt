package com.gumrindelwald.core.domain

import kotlin.time.Duration

const val HOUR_TO_SECOND = 3600
const val MINUTE_TO_SECOND = 60

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format("%02d", totalSeconds / HOUR_TO_SECOND)
    val minutes = String.format("%02d", (totalSeconds % HOUR_TO_SECOND) / MINUTE_TO_SECOND)
    val seconds = String.format("%02d", totalSeconds % MINUTE_TO_SECOND)

    println(" haha $totalSeconds $hours:$minutes:$seconds")

    return "$hours:$minutes:$seconds"
}