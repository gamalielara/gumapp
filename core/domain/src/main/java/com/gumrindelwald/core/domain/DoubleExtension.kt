package com.gumrindelwald.core.domain

import kotlin.math.pow
import kotlin.math.round

fun Double.roundToDecimals(decimalCount: Int): Double {
    val factor = 10f.pow(decimalCount)

    return round(this * factor) / factor
}