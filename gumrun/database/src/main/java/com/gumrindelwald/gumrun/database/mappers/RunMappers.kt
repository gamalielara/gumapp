package com.gumrindelwald.gumrun.database.mappers

import com.gumrindelwald.domain.RunLocation
import com.gumrindelwald.domain.run.Run
import com.gumrindelwald.gumrun.database.entity.RunEntity
import org.bson.types.ObjectId
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.milliseconds

fun RunEntity.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUTC = ZonedDateTime.parse(dateTimeUTC),
        distanceMeters = distanceMeters,
        location = RunLocation(lat, long),
        maxSpeedKmH = maxSpeedKmH,
        totalElevationMeters = totalElevationMeters,
        mapPictureURL = mapPictureURL
    )
}

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        durationMillis = duration.inWholeMilliseconds,
        dateTimeUTC = dateTimeUTC.toString(),
        distanceMeters = distanceMeters,
        lat = location.lat,
        long = location.long,
        avgSpeedKmH = avgSpeedKmH,
        maxSpeedKmH = maxSpeedKmH,
        totalElevationMeters = totalElevationMeters,
        mapPictureURL = mapPictureURL
    )
}
