package com.gumrindelwald.gumrun.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId

@Entity
data class RunEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = ObjectId().toHexString(),

    val durationMillis: Long,
    val distanceMeters: Int,
    val dateTimeUTC: String,
    val lat: Double,
    val long: Double,
    val avgSpeedKmH: Double,
    val maxSpeedKmH: Double,
    val totalElevationMeters: Int,
    val mapPictureURL: String?
)
