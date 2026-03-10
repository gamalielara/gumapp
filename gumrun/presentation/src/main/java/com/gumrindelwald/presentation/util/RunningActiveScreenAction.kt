package com.gumrindelwald.presentation.util

sealed interface RunningActiveScreenAction {
    data object OnToggleRunStatus : RunningActiveScreenAction
    data object OnFinishRun : RunningActiveScreenAction
    data object OnResumeRun : RunningActiveScreenAction
    data object OnBackClick : RunningActiveScreenAction
    data object DismissRationaleDialog : RunningActiveScreenAction

    data class SubmitLocationInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean
    ) : RunningActiveScreenAction

    data class SubmitNotificationPermissionInfo(
        val acceptedNotiPermission: Boolean,
        val showNotiRationale: Boolean
    ) : RunningActiveScreenAction
}
