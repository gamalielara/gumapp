package com.gumrindelwald.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumrindelwald.domain.RunningTracker
import com.gumrindelwald.presentation.util.ActiveRunState
import com.gumrindelwald.presentation.util.RunningActiveScreenAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    runningTracker: RunningTracker
) : ViewModel() {
    var state by mutableStateOf(ActiveRunState())
        private set

    private val shouldTrack = snapshotFlow { state.shouldTrack }.stateIn(
        viewModelScope,
        SharingStarted.Lazily, state.shouldTrack
    )

    private val _hasLocationPerm = MutableStateFlow(false)

    private val isTracking = combine(shouldTrack, _hasLocationPerm) { shouldTrack, hasLocPerm ->
        shouldTrack && hasLocPerm
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        _hasLocationPerm.onEach { hasPerm ->
            if (hasPerm) {
                runningTracker.startObserving()
            } else {
                runningTracker.stopObserving()

            }
        }.launchIn(viewModelScope)

        isTracking.onEach { isTracking -> runningTracker.setIsTracking(isTracking) }
            .launchIn(viewModelScope)


        // RunningTracker updating the state goes here
    }

    fun onAction(action: RunningActiveScreenAction) {
        when (action) {
            RunningActiveScreenAction.DismissRationaleDialog -> {}
            RunningActiveScreenAction.OnBackClick -> {}
            RunningActiveScreenAction.OnFinishRun -> {}
            RunningActiveScreenAction.OnResumeRun -> {}
            RunningActiveScreenAction.OnToggleRunStatus -> {}
        }
    }
}