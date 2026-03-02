package com.gumrindelwald.presentation.util

import com.gumrindelwald.domain.RunData
import com.gumrindelwald.domain.RunLocation
import kotlin.time.Duration

data class ActiveRunState(
    val elapsedTime: Duration = Duration.ZERO,
    val shouldTrack: Boolean = false,
    val isRunFinished: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val currentLocation: RunLocation? = null,
    val isSavingRun: Boolean = false,
    val showLocationPermissionRationale: Boolean = false,
    val showNotiPermissionRationale: Boolean = false,
    val runData: RunData = RunData()
)
