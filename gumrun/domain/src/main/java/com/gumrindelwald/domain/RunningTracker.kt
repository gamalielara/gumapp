package com.gumrindelwald.domain

import com.gumrindelwald.domain.location.LocationObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RunningTracker(
    private val observer: LocationObserver, private val appScope: CoroutineScope
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
    }
        .stateIn(appScope, SharingStarted.Lazily, null)

    init {
        _isTracking
            .onEach { isTracking ->
                if (!isTracking) {
                    // Stop tracking
                    // Add empty list to make the location not continuous
                    val newLocationList = buildList {
                        addAll(_runData.value.locations)
                        add(emptyList<RunLocationTimestamp>()) // To add gap between run
                    }

                    _runData.update {
                        it.copy(
                            locations = newLocationList
                        )
                    }
                }
            }
            .flatMapLatest { isTracking ->
                // Emit time if isTracking to update the elapses time
                if (isTracking) {
                    Timer.timeAndAEmit()
                } else {
                    flowOf() // Do nothing
                }
            }
            .onEach {
                _elapsedTime.value += it
            }
            .launchIn(appScope)

        currentLocationFlow
            .filterNotNull()
            .combineTransform(isTracking) { loc, isTracking ->
                if (isTracking) {
                    emit(loc)
                }
            }
            .zip(elapsedTime) { loc, elapsedTime ->
                RunLocationTimestamp(
                    location = loc,
                    duration = elapsedTime // Update the data with the new duration
                )
            }
            .onEach { locWithTimestamp ->
                // Location: [[loc1, loc2], [loc2, loc3], [loc3, loc4]]
                val currLocation = runData.value.locations
                val lastLocation =
                    if (currLocation.isNotEmpty())
                        currLocation.last() + locWithTimestamp // [loc3, loc4] + loc5
                    else listOf(locWithTimestamp)

                val newLocations = currLocation.replaceLast(lastLocation)

                val distanceInMeter = LocationCalculator.getTotalDistanceMeters(newLocations)
                val distanceInKM = distanceInMeter / 1000.0

                val pace =
                    if (distanceInKM == 0.0) 0
                    else (distanceInKM / elapsedTime.value.inWholeSeconds).roundToInt()


                _runData.update { data ->
                    data.copy(
                        locations = newLocations,
                        distanceMeters = distanceInMeter,
                        pace = pace.seconds,
                    )
                }

            }
            .launchIn(appScope)
    }


    fun startObservingLocation() {
        _isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        _isObservingLocation.value = false
    }

    fun setIsTracking(isTracking: Boolean) {
        _isTracking.value = isTracking
    }
}

private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
    if (isEmpty()) {
        return listOf(replacement)
    }

    return this.dropLast(1) + listOf(replacement)
}