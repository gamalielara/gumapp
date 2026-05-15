package com.gumrindelwald.presentation.run_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gumrindelwald.designsystem.GumAppTheme
import com.gumrindelwald.presentation.GumAppToolbar
import com.gumrindelwald.presentation.GumrunScaffold

@Composable

fun GumRunOverviewScreenRoot(
//    viewModel: RunOverviewViewModel = koinViewModel()
) {
    RunOverview(
//        state = viewModel.state,
        state = RunOverviewState(
            runs = listOf(
                RunUI(
                    id = "1",
                    duration = "1:00:00",
                    dateTime = "3 May 2025",
                    distance = "5 km",
                    avgSpeed = "10 km/h",
                    maxSpeed = "10 km/h",
                    pace = "10:00 min/km",
                    totalElevation = "10 m",
                    mapPictureURL = null,
                ),
                RunUI(
                    id = "2",
                    duration = "1:00:00",
                    dateTime = "3 May 2025",
                    distance = "5 km",
                    avgSpeed = "10 km/h",
                    maxSpeed = "10 km/h",
                    pace = "10:00 min/km",
                    totalElevation = "10 m",
                    mapPictureURL = null,
                ),
                RunUI(
                    id = "2",
                    duration = "1:00:00",
                    dateTime = "3 May 2025",
                    distance = "5 km",
                    avgSpeed = "10 km/h",
                    maxSpeed = "10 km/h",
                    pace = "10:00 min/km",
                    totalElevation = "10 m",
                    mapPictureURL = null,
                )
            ),
        ),
//        onAction = viewModel::onAction
        onAction = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RunOverview(
    state: RunOverviewState, onAction: (action: RunOverviewAction) -> Unit
) {
    GumrunScaffold(
        topAppBar = {
            GumAppToolbar(
                showBackButton = true,
                title = "Recent Runs",
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding() + 10.dp,
                        start = 10.dp,
                        end = 10.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                state.runs.forEach { runUI ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(60.dp)
                                    .padding(10.dp)
                                    .clip(
                                        RoundedCornerShape(10.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.tertiary),
                                contentAlignment = Alignment.Center
                            ) {
                                // TODO: change to run map marker
                                Icon(
                                    imageVector = Icons.Default.DirectionsRun,
                                    tint = MaterialTheme.colorScheme.onTertiary,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(4f)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    "Evening Run",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    runUI.dateTime,
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    fontSize = 12.sp
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    RunPill(runUI.pace)
                                    RunPill(runUI.distance)
                                    RunPill(runUI.duration)
                                }
                            }
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                ),
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp),
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier
                                        .width(20.dp)
                                        .aspectRatio(1f)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun RunPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = .5f))
            .padding(horizontal = 5.dp)
    ) {
        Text(text, fontSize = 10.sp, color = MaterialTheme.colorScheme.onTertiary)
    }
}

@Preview
@Composable
private fun RunOverviewPreview() {
    GumAppTheme {
        RunOverview(
            state = RunOverviewState(
                runs = listOf(
                    RunUI(
                        id = "1",
                        duration = "1:00:00",
                        dateTime = "3 May 2025",
                        distance = "5 km",
                        avgSpeed = "10 km/h",
                        maxSpeed = "10 km/h",
                        pace = "10:00 min/km",
                        totalElevation = "10 m",
                        mapPictureURL = null,
                    ),
                    RunUI(
                        id = "2",
                        duration = "1:00:00",
                        dateTime = "3 May 2025",
                        distance = "5 km",
                        avgSpeed = "10 km/h",
                        maxSpeed = "10 km/h",
                        pace = "10:00 min/km",
                        totalElevation = "10 m",
                        mapPictureURL = null,
                    ),
                    RunUI(
                        id = "2",
                        duration = "1:00:00",
                        dateTime = "3 May 2025",
                        distance = "5 km",
                        avgSpeed = "10 km/h",
                        maxSpeed = "10 km/h",
                        pace = "10:00 min/km",
                        totalElevation = "10 m",
                        mapPictureURL = null,
                    )
                ),
            ),
            onAction = {}
        )
    }
}