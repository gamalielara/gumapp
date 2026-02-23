package com.gumrindelwald.presentation

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.gumrindelwald.gumapp.core.presentation.designsystem.R

@Composable

fun GumrunDashboardRoot(
) {
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
            context,
            com.gumrindelwald.gumrun.presentation.R.raw.map_style
        )
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    GumrunScaffold(
        withGradient = true,
        topAppBar = {
            GumAppToolbar(
                showBackButton = true,
                title = stringResource(R.string.active_run),
                onBackClick = {},
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
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
                })
            {

            }
        }
    }
}

@Preview
@Composable
private fun DashboardPreview() {
    Dashboard(
    )

}