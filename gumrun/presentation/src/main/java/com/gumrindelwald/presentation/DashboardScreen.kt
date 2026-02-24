package com.gumrindelwald.presentation

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.gumrindelwald.gumapp.core.presentation.designsystem.R
import org.koin.androidx.compose.koinViewModel


@Composable

fun GumrunDashboardRoot(
) {
    val viewModel: DashboardViewModel = koinViewModel()

    Log.d("test", "Haha ${viewModel.state}")

    Dashboard(
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Dashboard(
) {
    val context = LocalContext.current

    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(
            context, com.gumrindelwald.gumrun.presentation.R.raw.map_style
        )
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    handleLocationNotiPermission()

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

            }
        }
    }
}

@Composable
private fun handleLocationNotiPermission() {
    val context = LocalContext.current

    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCourseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotiPermission =
            if (Build.VERSION.SDK_INT >= 33) perms[Manifest.permission.POST_NOTIFICATIONS] == true else true


        val activity = context as ComponentActivity
        val showLocationPermissionRationale =
            shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        val showNotiPermissionRationale =
            Build.VERSION.SDK_INT >= 33 && shouldShowRequestPermissionRationale(
                activity, Manifest.permission.POST_NOTIFICATIONS
            )

        Log.d(showLocationPermissionRationale.toString(), showNotiPermissionRationale.toString())

        if (!hasCourseLocationPermission || !hasFineLocationPermission || !hasNotiPermission) {

        }
    }
}

@Preview
@Composable
private fun DashboardPreview() {
    Dashboard(
    )

}