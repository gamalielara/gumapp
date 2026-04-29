package com.gumrindelwald.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.maps.android.compose.GoogleMapComposable
import com.gumrindelwald.core.domain.formatted
import com.gumrindelwald.core.domain.roundToDecimals
import com.gumrindelwald.designsystem.GumAppActionButton
import com.gumrindelwald.designsystem.GumAppDialog
import com.gumrindelwald.gumapp.core.presentation.designsystem.R
import com.gumrindelwald.presentation.util.ActiveRunState
import com.gumrindelwald.presentation.util.KILOMETER_TO_METER
import com.gumrindelwald.presentation.util.RunningActiveScreenAction
import com.gumrindelwald.presentation.util.RunningTrackerService
import com.gumrindelwald.presentation.util.toFormattedPace
import org.koin.androidx.compose.koinViewModel
import java.io.ByteArrayOutputStream


@Composable

fun GumrunDashboardRoot(
    mainActivityClass: Class<*>,
    onRunFinished: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: DashboardViewModel = koinViewModel()
    val context = LocalContext.current

    ObserveAsEvents(flow = viewModel.eventFlow) { event ->
        when (event) {
            is ActiveRunEventChannel.Error -> Toast.makeText(
                context,
                event.error.asString(context),
                Toast.LENGTH_LONG
            ).show()

            ActiveRunEventChannel.RunSaved -> {
                onRunFinished()
            }
        }
    }

    Dashboard(
        state = viewModel.state,
        onClick = { action ->
            when (action) {
                is RunningActiveScreenAction.OnBackClick -> {
                    if (!viewModel.state.hasStartedRunning) {
                        onBack()
                    }
                }

                else -> Unit
            }
            viewModel.onAction(action)
        },
        mainActivityClass
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@GoogleMapComposable
private fun Dashboard(
    state: ActiveRunState,
    onClick: (RunningActiveScreenAction) -> Unit,
    mainActivityClass: Class<*>,
) {
    val context = LocalContext.current
    val currentLocation = state.currentLocation

    val permLauncher = handleLocationNotiPermission(onAction = onClick)

    LaunchedEffect(true) {
        val activity = context as Activity

        val hasCourseLocationPermission =
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasFineLocationPermission =
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= 33) context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED else true
        val hasLocationPermission = hasFineLocationPermission && hasCourseLocationPermission


        val shouldShowLocationPermissionRationale = shouldShowRequestPermissionRationale(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val shouldShowNotiPermissionRationale =
            Build.VERSION.SDK_INT >= 33 && shouldShowRequestPermissionRationale(
                activity, Manifest.permission.POST_NOTIFICATIONS
            )

        onClick(
            RunningActiveScreenAction.SubmitLocationInfo(
                acceptedLocationPermission = hasLocationPermission,
                showLocationRationale = shouldShowLocationPermissionRationale,
            )
        )

        onClick(
            RunningActiveScreenAction.SubmitNotificationPermissionInfo(
                showNotiRationale = shouldShowNotiPermissionRationale
            )
        )

        if (!shouldShowLocationPermissionRationale && !shouldShowNotiPermissionRationale) {
            permLauncher.requestGumrunPermissions(context)
        }
    }

    GumrunScaffold(
        withGradient = false, topAppBar = {
            GumAppToolbar(
                showBackButton = true,
                title = stringResource(R.string.active_run),
                onBackClick = {},
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding()
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
        ) {
            GumrunMap(
                currentLocation = state.currentLocation,
                allLocations = state.runData.locations,
                isRunFinished = state.isRunFinished,
                modifier = Modifier,
                context = context,
                onSnapshot = { bmp ->
                    val stream = ByteArrayOutputStream()
                    stream.use {
                        bmp.compress(
                            Bitmap.CompressFormat.JPEG, 100, it
                        )
                    }

                    onClick(RunningActiveScreenAction.OnRunProcessed(stream.toByteArray()))
                }
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        vertical = 100.dp + 10.dp,
                        horizontal = paddingValues.calculateBottomPadding()
                    )
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Button(
                    onClick = {
                        // TODO
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Button(
                    onClick = {
                        onClick(RunningActiveScreenAction.OnToggleRunStatus)

                        // The lambda still refers to the old shouldTrack value
                        // Fire only once
                        if (!state.hasStartedRunning) {
                            context.startService(
                                RunningTrackerService.createStartIntent(
                                    context, mainActivityClass
                                )
                            )
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    Icon(
                        imageVector = if (state.shouldTrack) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        vertical = paddingValues.calculateBottomPadding() + 10.dp,
                        horizontal = paddingValues.calculateBottomPadding()
                    )
                    .clip(shape = RoundedCornerShape(20.dp))
                    .height(100.dp)
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(10f)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = .75f))
                        .blur(100.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(100f)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(7f),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(R.string.time_elapsed),
                                fontSize = 12.sp,
                            )
                            Text(
                                text = state.elapsedTime.formatted(),
                                fontSize = 18.sp,
                            )

                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(R.string.distance),
                                fontSize = 12.sp,
                            )
                            Text(
                                text = (state.runData.distanceMeters / KILOMETER_TO_METER).roundToDecimals(
                                    2
                                ).toString() + " km",
                                fontSize = 18.sp,
                            )

                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(R.string.pace),
                                fontSize = 12.sp,
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom,
                            ) {
                                Text(
                                    text = state.elapsedTime.toFormattedPace(
                                        (state.runData.distanceMeters / KILOMETER_TO_METER)
                                    ),
                                    fontSize = 18.sp,
                                )
                                if (state.runData.distanceMeters > 0) {
                                    Text(
                                        text = "/km",
                                        fontSize = 12.sp,
                                    )
                                }
                            }
                        }
                    }

                }
            }

        }


    }

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

                        permLauncher.requestGumrunPermissions(context)
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
                showNotiRationale = shouldShowNotiPermissionRationale
            )
        )
    }

    return permissionLauncher
}

private fun ActivityResultLauncher<Array<String>>.requestGumrunPermissions(
    context: Context,
) {
    val hasLocationPermission = context.checkHaslocationPermission()
    val hasNotiPermission = context.hasNotiPermission()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notiPermission =
        if (Build.VERSION.SDK_INT >= 33) arrayOf(Manifest.permission.POST_NOTIFICATIONS) else emptyArray()

    when {
        !hasLocationPermission && !hasNotiPermission -> {
            launch(locationPermissions + notiPermission)
        }

        !hasLocationPermission -> launch(locationPermissions)
        !hasNotiPermission -> launch(notiPermission)
    }
}

private fun Context.hasNotiPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= 33) {
        checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

private fun Context.checkHaslocationPermission(): Boolean {
    return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

}


@Preview
@Composable
private fun DashboardPreview() {
    Dashboard(
        state = ActiveRunState(), onClick = {}, mainActivityClass = ComponentActivity::class.java
    )

}
