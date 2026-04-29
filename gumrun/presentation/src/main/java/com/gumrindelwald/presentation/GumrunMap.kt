package com.gumrindelwald.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.ktx.awaitSnapshot
import com.gumrindelwald.designsystem.RunIcon
import com.gumrindelwald.domain.RunLocation
import com.gumrindelwald.domain.RunLocationTimestamp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class,
    DelicateCoroutinesApi::class
)
@Composable
@GoogleMapComposable
fun GumrunMap(
    currentLocation: RunLocation?,
    allLocations: List<List<RunLocationTimestamp>>,
    isRunFinished: Boolean,
    modifier: Modifier = Modifier,
    context: Context,
    onSnapshot: (Bitmap) -> Unit
) {
    val markerState = rememberMarkerState()

    val cameraPositionState = rememberCameraPositionState()
    var isMapLoaded by remember { mutableStateOf(false) }

    val markerPositionLat by animateFloatAsState(
        targetValue = currentLocation?.lat?.toFloat() ?: 0f, animationSpec = tween(300)
    )

    val markerPositionLng by animateFloatAsState(
        targetValue = currentLocation?.long?.toFloat() ?: 0f, animationSpec = tween(300)
    )

    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(
            context, com.gumrindelwald.gumrun.presentation.R.raw.map_style
        )
    }

    var triggerCapture by remember {
        mutableStateOf(false)
    }

    var createSnapshotJob = remember<Job?> {
        null
    }


    LaunchedEffect(isMapLoaded, markerPositionLat, markerPositionLng) {
        markerState.position = LatLng(markerPositionLat.toDouble(), markerPositionLng.toDouble())

    }

    LaunchedEffect(isMapLoaded, currentLocation) {
        if (isMapLoaded) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        currentLocation?.lat?.toDouble() ?: 0.0,
                        currentLocation?.long?.toDouble() ?: 0.0
                    ), 20f
                )
            )
        }
    }


    LaunchedEffect(isMapLoaded, currentLocation) {
        if (isMapLoaded) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        currentLocation?.lat?.toDouble() ?: 0.0,
                        currentLocation?.long?.toDouble() ?: 0.0
                    ), 20f
                )
            )
        }
    }


    GoogleMap(
        cameraPositionState = cameraPositionState, properties = MapProperties(
            mapStyleOptions = mapStyle
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        ),
        onMapLoaded = {
            isMapLoaded = true
        },
        modifier = if (isRunFinished) {
            Modifier
                .size(300.dp)
                .aspectRatio(16 / 9f)
                .alpha(0f)
                .onSizeChanged {
                    if (it.width >= 300) {
                        triggerCapture = true
                    }
                }
        } else modifier) {

        GumRunPolylines(location = allLocations)

        MapEffect(currentLocation, isRunFinished, triggerCapture, createSnapshotJob) { map ->
            if (isRunFinished && triggerCapture && createSnapshotJob == null) {
                triggerCapture = false

                // Arange zoom to focus on the locations
                val boundBuilder = LatLngBounds.Builder()
                allLocations.flatten()
                    .forEach { location ->
                        boundBuilder.include(
                            LatLng(
                                location.location.lat,
                                location.location.long
                            )
                        )
                    }

                map.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        boundBuilder.build(),
                        100
                    )
                )

                map.setOnCameraIdleListener {
                    createSnapshotJob?.cancel()
                    createSnapshotJob = GlobalScope.launch {
                        // A bit delay to allow the map to settle
                        delay(500L)
                        map.awaitSnapshot()?.let(onSnapshot)
                    }
                }

                val bitmap = map.awaitSnapshot()
            }
        }

        if (currentLocation != null) {
            MarkerComposable(
                currentLocation, state = markerState
            ) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = RunIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}