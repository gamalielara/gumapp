package com.gumrindelwald.domain

import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationCalculator {
    fun getTotalDistanceMeters(locations: List<List<RunLocationTimestamp>>): Int {
        return locations.sumOf { timestampPerLine ->
            timestampPerLine.zipWithNext { loc1, loc2 ->
                loc1.location.distanceTo(loc2.location)
            }.sum().roundToInt()
        }
    }

    fun getMaxSpeedKmH(locations: List<List<RunLocationTimestamp>>): Double {
        return locations.maxOf { locSet ->
            locSet.zipWithNext { loc1, loc2 ->
                val distance = loc1.location.distanceTo(loc2.location)
                val hoursDiffer = (loc2.duration - loc1.duration).toDouble(DurationUnit.HOURS)

                if (hoursDiffer == 0.0) 0.0 else {
                    val distanceInKM = distance / 1000.0
                    distanceInKM / hoursDiffer
                }
            }.maxOrNull() ?: 0.0
        }
    }

    fun getTotalElevationMeters(locations: List<List<RunLocationTimestamp>>): Int {
        return locations.sumOf { locSet ->
            locSet.zipWithNext { loc1, loc2 ->
                val altitude1 = loc1.location.altitude
                val altitude2 = loc2.location.altitude

                (altitude2 - altitude1)
            }.sum().roundToInt()
        }
    }
}