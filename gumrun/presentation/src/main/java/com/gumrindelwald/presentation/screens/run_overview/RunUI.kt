package com.gumrindelwald.presentation.screens.run_overview

data class RunUI(
    val id: String,
    val duration: String,
    val dateTime: String,
    val distance: String,
    val avgSpeed: String,
    val maxSpeed: String,
    val pace: String,
    val totalElevation: String,
    val mapPictureURL: String?,
    val polylineRoute: String?,
)