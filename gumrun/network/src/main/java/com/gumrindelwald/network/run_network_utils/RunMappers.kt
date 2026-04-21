package com.gumrindelwald.network.run_network_utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.gumrindelwald.domain.RunLocation
import com.gumrindelwald.domain.run.Run
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

//val test = Instant.parse(dateTimeUTC).atZone(ZoneId.of("UTC"))

@RequiresApi(Build.VERSION_CODES.O)
fun RunDTO.toRun() = Run(
    id = id,
    duration = durationMillis.milliseconds,
    dateTimeUTC = Instant.parse(dateTimeUTC).atZone(ZoneId.of("UTC")),
    distanceMeters = distanceMeters,
    location = RunLocation(lat, long),
    maxSpeedKmH = maxSpeedKmH,
    totalElevationMeters = totalElevationMeters,
    mapPictureURL = mapPictureURL
)

@RequiresApi(Build.VERSION_CODES.O)
fun Run.toRunRequest() = CreateRunRequest(
    id = id!!,
    durationMillis = duration.inWholeMilliseconds,
    distanceMeters = distanceMeters,
    lat = location.lat,
    long = location.long,
    avgSpeedKmH = avgSpeedKmH,
    maxSpeedKmH = maxSpeedKmH,
    mapPictureURL = mapPictureURL,
    epochMillis = dateTimeUTC.toEpochSecond() * 1000L,
    totalElevationMeters = totalElevationMeters,
)