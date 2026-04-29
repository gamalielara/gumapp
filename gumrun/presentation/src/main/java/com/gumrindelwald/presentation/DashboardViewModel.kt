package com.gumrindelwald.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumrindelwald.core.domain.util.Result
import com.gumrindelwald.domain.LocationCalculator
import com.gumrindelwald.domain.RunLocation
import com.gumrindelwald.domain.RunningTracker
import com.gumrindelwald.domain.run.Run
import com.gumrindelwald.domain.run.RunRepository
import com.gumrindelwald.presentation.util.ActiveRunState
import com.gumrindelwald.presentation.util.RunningActiveScreenAction
import com.gumrindelwald.presentation.util.RunningStatusTracker
import com.gumrindelwald.presentation.util.RunningTrackerService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class DashboardViewModel(
    val runningTracker: RunningTracker,
    val runningStatusTracker: RunningStatusTracker,
    val runRepository: RunRepository
) : ViewModel() {
    val _eventChannel = Channel<ActiveRunEventChannel>()
    val eventFlow = _eventChannel.receiveAsFlow()

    var state by mutableStateOf(
        ActiveRunState(
            shouldTrack = RunningTrackerService.isTracking
        )
    )
        private set

    private val shouldTrack = snapshotFlow { state.shouldTrack }.stateIn(
        viewModelScope, SharingStarted.Lazily, state.shouldTrack
    )

    private val _hasLocationPerm = MutableStateFlow(false)

    private val isTracking = combine(shouldTrack, _hasLocationPerm) { shouldTrack, hasLocPerm ->
        shouldTrack && hasLocPerm
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        _hasLocationPerm.onEach { hasPerm ->
            if (hasPerm) {
                runningTracker.startObservingLocation()
            } else {
                runningTracker.stopObservingLocation()
            }
        }.launchIn(viewModelScope)

        isTracking.onEach { isTracking ->
            runningTracker.setIsTracking(isTracking)
        }.launchIn(viewModelScope)


        // RunningTracker updating the state goes here
        runningTracker.currentLocationFlow.onEach {
            state = state.copy(currentLocation = it)
        }.launchIn(viewModelScope)

        runningTracker.runData.onEach {
            state = state.copy(runData = it)
        }.launchIn(viewModelScope)

        runningTracker.elapsedTime.onEach {
            state = state.copy(elapsedTime = it)
        }.launchIn(viewModelScope)

        runningStatusTracker.isRunning.onEach {
            state = state.copy(shouldTrack = it)
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RunningActiveScreenAction) {
        when (action) {
            RunningActiveScreenAction.DismissRationaleDialog -> {
                state = state.copy(
                    showNotiPermissionRationale = false, showLocationPermissionRationale = false
                )
            }

            RunningActiveScreenAction.OnBackClick -> {}

            RunningActiveScreenAction.OnFinishRun -> {
                state = state.copy(isRunFinished = true, isSavingRun = true)
            }

            RunningActiveScreenAction.OnResumeRun -> {
                // When the state shouldTrack is changed, the flow that listens to shouldTrack & hasLocPerm will set the runningTracker to stop observing location
                state = state.copy(shouldTrack = true)
            }

            RunningActiveScreenAction.OnToggleRunStatus -> {
                if (!state.hasStartedRunning && !state.shouldTrack) {
                    state = state.copy(hasStartedRunning = true)
                }

                if (state.shouldTrack) {
                    runningStatusTracker.stopTracking()
                } else {
                    runningStatusTracker.startTracking()
                }
            }

            is RunningActiveScreenAction.OnRunProcessed -> {
                finishRun(action.mapPictureBytes)
            }

            is RunningActiveScreenAction.SubmitLocationInfo -> {
                _hasLocationPerm.value = action.acceptedLocationPermission

                state = state.copy(
                    showLocationPermissionRationale = action.showLocationRationale
                )
            }

            is RunningActiveScreenAction.SubmitNotificationPermissionInfo -> {
                state = state.copy(
                    showNotiPermissionRationale = action.showNotiRationale
                )
            }

        }

    }

    private fun finishRun(mapPictureBytes: ByteArray) {
        val locations = state.runData.locations

        // Np location tracked, or single location
        if (locations.isEmpty() || locations.first().size <= 1) {
            state = state.copy(isSavingRun = false)
            return;
        }

        state = state.copy(isSavingRun = true)
        viewModelScope.launch {
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUTC = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                distanceMeters = state.runData.distanceMeters,
                location = state.currentLocation ?: RunLocation(0.0, 0.0),
                maxSpeedKmH = LocationCalculator.getMaxSpeedKmH(state.runData.locations),
                totalElevationMeters = LocationCalculator.getTotalElevationMeters(state.runData.locations),
                mapPictureURL = null,
            )


            when (val result = runRepository.upsertRun(run, mapPictureBytes)) {
                is Result.Error -> {
                    _eventChannel.send(ActiveRunEventChannel.Error(result.error.asUIText()))
                }

                is Result.Success<*> -> {
                    _eventChannel.send(ActiveRunEventChannel.RunSaved)
                }
            }
        }
        state = state.copy(isSavingRun = false)
        runningTracker.finishRun()
        runningStatusTracker.stopTracking()
    }
}