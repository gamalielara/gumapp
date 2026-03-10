package com.gumrindelwald.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberMarkerState
import com.gumrindelwald.designsystem.GumAppActionButton
import com.gumrindelwald.designsystem.GumAppDialog
import com.gumrindelwald.designsystem.RunIcon
import com.gumrindelwald.gumapp.core.presentation.designsystem.R
import com.gumrindelwald.presentation.util.ActiveRunState
import com.gumrindelwald.presentation.util.RunningActiveScreenAction
import org.koin.androidx.compose.koinViewModel


@Composable

fun GumrunDashboardRoot(
) {
    val viewModel: DashboardViewModel = koinViewModel()

    Log.d("test", "Haha ${viewModel.state}")

    Dashboard(
        state = viewModel.state, onClick = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Dashboard(
    state: ActiveRunState, onClick: (RunningActiveScreenAction) -> Unit
) {
    val context = LocalContext.current
    val currentLocation = state.currentLocation

    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(
            context, com.gumrindelwald.gumrun.presentation.R.raw.map_style
        )
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    val markerState = rememberMarkerState()

    val markerPositionLat by animateFloatAsState(
        targetValue = currentLocation?.lat?.toFloat() ?: 0f, animationSpec = tween(500)
    )

    val markerPositionLng by animateFloatAsState(
        targetValue = currentLocation?.long?.toFloat() ?: 0f, animationSpec = tween(500)
    )

    LaunchedEffect(isMapLoaded, markerPositionLat, markerPositionLng) {
        markerState.position = LatLng(markerPositionLat.toDouble(), markerPositionLng.toDouble())
        Log.d("test", "Haha ${markerState.position}")
        Log.d("test", "Haha ${currentLocation}")
    }

    val permLauncher = handleLocationNotiPermission(onAction = onClick)

    GumrunScaffold(
        withGradient = false, topAppBar = {
            GumAppToolbar(
                showBackButton = true,
                title = stringResource(R.string.active_run),
                onBackClick = {},
            )
        }) {
        Box(
            modifier = Modifier
                .padding()
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
        ) {
            GoogleMap(
                properties = MapProperties(
                    mapStyleOptions = mapStyle
                ), uiSettings = MapUiSettings(
                    zoomControlsEnabled = false
                ), onMapLoaded = {
                    isMapLoaded = true
                }) {
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
    }

    Log.d(
        "test",
        "Haha $state"
    )

    if (state.showNotiPermissionRationale || state.showLocationPermissionRationale) {
        GumAppDialog(
            title = stringResource(R.string.permission),
            onDismiss = {/* dismiss is not allowed */ },
            description = "",
            primaryButton = {
                GumAppActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    modifier = Modifier,
                    onClick = {
                        onClick(RunningActiveScreenAction.DismissRationaleDialog)

                        val hasCourseLocationPermission =
                            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        val hasFineLocationPermission =
                            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                        val hasNotiPermission =
                            if (Build.VERSION.SDK_INT >= 33) context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED else true
                        val hasLocationPermission =
                            hasFineLocationPermission && hasCourseLocationPermission

                        val locationPermissions = arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )

                        val notiPermission = if (Build.VERSION.SDK_INT >= 33) arrayOf(
                            Manifest.permission.POST_NOTIFICATIONS
                        ) else emptyArray()

                        var permissionToAsk: Array<String> = arrayOf()

                        if (!hasLocationPermission && !hasNotiPermission) {
                            permissionToAsk = notiPermission + locationPermissions
                        } else if (!hasNotiPermission) {
                            permissionToAsk = notiPermission
                        } else if (!hasLocationPermission) {
                            permissionToAsk = locationPermissions
                        }

                        permLauncher.launch(permissionToAsk)
                    })
            },
            modifier = Modifier,
        )
    }
}

@Composable
private fun handleLocationNotiPermission(onAction: (RunningActiveScreenAction) -> Unit): ActivityResultLauncher<Array<String>> {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCourseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotiPermission =
            if (Build.VERSION.SDK_INT >= 33) perms[Manifest.permission.POST_NOTIFICATIONS] == true else true


        val activity = context as ComponentActivity

        val shouldShowLocationPermissionRationale = shouldShowRequestPermissionRationale(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val shouldShowNotiPermissionRationale =
            Build.VERSION.SDK_INT >= 33 && shouldShowRequestPermissionRationale(
                activity, Manifest.permission.POST_NOTIFICATIONS
            )

        onAction(
            RunningActiveScreenAction.SubmitLocationInfo(
                acceptedLocationPermission = hasCourseLocationPermission && hasFineLocationPermission,
                showLocationRationale = shouldShowLocationPermissionRationale,
            )
        )

        onAction(
            RunningActiveScreenAction.SubmitNotificationPermissionInfo(
                acceptedNotiPermission = hasNotiPermission,
                showNotiRationale = shouldShowNotiPermissionRationale
            )
        )
    }

    return permissionLauncher
}

@Preview
@Composable
private fun DashboardPreview() {
    Dashboard(
        state = ActiveRunState(), onClick = {})

}