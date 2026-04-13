package com.gumrindelwald.presentation.run_overview.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.gumrindelwald.designsystem.GumAppTheme
import com.gumrindelwald.domain.RunLocation
import com.gumrindelwald.domain.run.Run
import com.gumrindelwald.gumapp.core.presentation.designsystem.R
import com.gumrindelwald.presentation.GumAppToolbar
import com.gumrindelwald.presentation.GumrunScaffold
import com.gumrindelwald.presentation.run.RunUI
import com.gumrindelwald.presentation.run.toRunUI
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunListItem(
    runUI: RunUI, onDeleteClick: () -> Unit, modifier: Modifier = Modifier
) {
    GumrunScaffold(
        topAppBar = {
            GumAppToolbar(
                showBackButton = true,
                title = stringResource(R.string.run_detail),
                onBackClick = onDeleteClick,
            )
        }) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MapImage(
                imageURL = runUI.mapPictureURL, modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Sunday, April 12 2026 5:30 PM",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    maxItemsInEachRow = 3,
                    verticalArrangement = Arrangement.spacedBy(10.dp),

                    ) {
                    RunDataGrid(runUI = runUI)
                }
            }
        }
    }
}

@Composable
private fun RunDataGrid(runUI: RunUI) {
    val runData = listOf(
        RunDataUI(
            name = stringResource(R.string.distance), value = runUI.distance
        ),
        RunDataUI(
            name = stringResource(R.string.duration), value = runUI.duration
        ),
        RunDataUI(
            name = stringResource(R.string.pace), value = runUI.pace
        ),
        RunDataUI(
            name = stringResource(R.string.avg_speed), value = runUI.avgSpeed
        ),
        RunDataUI(
            name = stringResource(R.string.max_speed), value = runUI.maxSpeed
        ),
        RunDataUI(
            name = stringResource(R.string.elevation), value = runUI.totalElevation
        ),
    )

    runData.forEach {
        DataGridCell(it)
    }
}

@Composable
private fun DataGridCell(
    runData: RunDataUI,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = runData.name,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = runData.value,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun MapImage(
    imageURL: String?, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = imageURL,
            contentDescription = stringResource(R.string.run_map),
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(6 / 9f)
                .clip(RoundedCornerShape(20.dp)),
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(stringResource(R.string.could_load_image))
                }
            }
        )


    }
}

@Preview
@Composable
fun RunListItemPreview() {
    GumAppTheme {
        RunListItem(
            runUI = Run(
                id = "1",
                duration = 10.minutes + 30.seconds,
                dateTimeUTC = ZonedDateTime.now(),
                distanceMeters = 5000,
                location = RunLocation(0.0, 0.0),
                maxSpeedKmH = 15.28372,
                totalElevationMeters = 12,
                mapPictureURL = null
            ).toRunUI(),
            onDeleteClick = {},
        )
    }
}
