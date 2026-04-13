package com.gumrindelwald.presentation.run

import com.gumrindelwald.core.domain.formatted
import com.gumrindelwald.domain.run.Run
import com.gumrindelwald.presentation.util.toFormattedKilometers
import com.gumrindelwald.presentation.util.toFormattedKmH
import com.gumrindelwald.presentation.util.toFormattedMeters
import com.gumrindelwald.presentation.util.toFormattedPace
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUI(): RunUI {
    val dateTimeInLocalTime = dateTimeUTC
        .withZoneSameLocal(ZoneId.systemDefault())

    val formattedDateTime = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val distanceKM = distanceMeters / 1000.0

    return RunUI(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKM.toFormattedKilometers(),
        avgSpeed = avgSpeedKmH.toFormattedKmH(),
        maxSpeed = maxSpeedKmH.toFormattedKmH(),
        pace = duration.toFormattedPace(distanceKM),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureURL = mapPictureURL,
    )
}