package com.gumrindelwald.presentation.util

import com.gumrindelwald.core.domain.MINUTE_TO_SECOND
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

const val KILOMETER_TO_METER = 1000.0

fun Double.toFormattedKilometers(): String = "${this.roundToDecimals(1)} km"

fun Double.toFormattedKmH(): String = "${roundToDecimals(1)} km/h"

fun Int.toFormattedMeters(): String = "$this m"

fun Duration.toFormattedPace(distanceKm: Double): String {
    if (this == Duration.ZERO || distanceKm <= 0.0) {
        return "-"
    }

    val secondsPerKm = (this.inWholeSeconds / distanceKm).roundToInt()
    val averagePaceInMinutes = secondsPerKm / MINUTE_TO_SECOND
    // Remaining seconds
    val averagePaceSeconds = String.format("%02d", secondsPerKm % MINUTE_TO_SECOND)

    return "$averagePaceInMinutes:$averagePaceSeconds"
}


private fun Double.roundToDecimals(decimalCount: Int): Double {
    val factor = 10f.pow(decimalCount)

    return round(this * factor) / factor
}