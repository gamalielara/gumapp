package com.gumrindelwald.domain

import com.gumrindelwald.domain.location.LocationObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class RunningTracker(
    private val observer: LocationObserver,
    private val appScope: CoroutineScope
) {
    private val _runData = MutableStateFlow(RunData())
    private val _isObservingLocation = MutableStateFlow<Boolean>(false)
    private val _isTracking = MutableStateFlow(false)
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)

    val runData = _runData.asStateFlow()
    val isTracking = _isTracking.asStateFlow()
    val elapsedTime = _elapsedTime.asStateFlow()

    val currentLocationFlow = _isObservingLocation.flatMapLatest { isObserving ->
        if (isObserving) {
            observer.observeLocation(1000L)
        } else {
            flowOf()
        }
    }.stateIn(appScope, SharingStarted.Lazily, null)

    init {
        _isTracking.onEach { isTracking ->
            if (!isTracking) {
                // Stop tracking
                // Add empty list to make the location not continuous
                val newLocationList = buildList {
                    addAll(_runData.value.locations)
                    add(emptyList<RunLocation>()) // To add gap between run
                }

                _runData.update {
                    it.copy(
                        locations = newLocationList
                    )
                }
            }
        }.flatMapLatest { isTracking ->
            // Emit time if isTracking to update the elapses time
            if (isTracking) {
                Timer.timeAndAEmit()
            } else {
                flowOf() // Do nothing
            }
        }.onEach {
            _elapsedTime.value += it
        }.launchIn(appScope)
    }


    fun startObserving() {}

    fun stopObserving() {}

    fun setIsTracking(isTracking: Boolean) {}
}