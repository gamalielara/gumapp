package com.gumrindelwald.domain

import kotlin.math.roundToInt

object LocationCalculator {
    fun getTotalDistanceMeters(locations: List<List<RunLocationTimestamp>>): Int {
        return locations
            .sumOf { timestampPerLine ->
                timestampPerLine
                    .zipWithNext { loc1, loc2 ->
                        loc1.location.distanceTo(loc2.location)
                    }
                    .sum()
                    .roundToInt()
            }
    }
}