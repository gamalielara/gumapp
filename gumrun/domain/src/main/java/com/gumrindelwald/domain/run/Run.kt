package com.gumrindelwald.domain.run

import com.gumrindelwald.domain.RunLocation
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class Run(
    val id: String?,
    val duration: Duration,
    val dateTimeUTC: ZonedDateTime,
    val distanceMeters: Int,
    val location: RunLocation,
    val maxSpeedKmH: Double,
    val totalElevationMeters: Int,
    val mapPictureURL: String?,

    ) {
    val avgSpeedKmH: Double
        get() {
            return distanceMeters / 1000 / duration.toDouble(DurationUnit.HOURS)
        }
}
