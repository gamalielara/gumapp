package com.gumrindelwald.network.run_network_utils

import kotlinx.serialization.Serializable

@Serializable
data class RunDTO(
    val id: String,
    val dateTimeUTC: String,
    val durationMillis: Long,
    val distanceMeters: Int,
    val lat: Double,
    val long: Double,
    val avgSpeedKmH: Double,
    val maxSpeedKmH: Double,
    val totalElevationMeters: Int,
    val mapPictureURL: String?
)