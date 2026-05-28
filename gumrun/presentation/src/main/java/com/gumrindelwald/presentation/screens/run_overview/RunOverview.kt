package com.gumrindelwald.presentation.screens.run_overview

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.PolyUtil
import com.gumrindelwald.designsystem.GumAppTheme
import com.gumrindelwald.presentation.GumAppToolbar
import com.gumrindelwald.presentation.GumrunScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun GumRunOverviewScreenRoot(
    viewModel: RunOverviewViewModel = koinViewModel(),
    onStartClick: () -> Unit
) {
    RunOverview(
        state = viewModel.state,
        onAction = {
            when (it) {
                is RunOverviewAction.OnStartClick -> {
                    onStartClick()
                }

                else -> viewModel::onAction
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RunOverview(
    state: RunOverviewState,
    onAction: (action: RunOverviewAction) -> Unit
) {
    GumrunScaffold(
        topAppBar = {
            GumAppToolbar(
                showBackButton = true,
                title = "Recent Runs",
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    onAction(RunOverviewAction.OnStartClick)
                },
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                contentPadding = PaddingValues(5.dp)
            ) {
                Icon(
                    Icons.Default.PlayArrow, tint = Color.White,
                    contentDescription = "Play",
                )
            }
        }
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
                                RunRouteThumbnail(runUI.polylineRoute)
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

@Composable
fun RunRouteThumbnail(
    encodedPolyline: String?,
    modifier: Modifier = Modifier
) {
    val points = remember(encodedPolyline) {
        encodedPolyline?.let { PolyUtil.decode(it) } ?: emptyList()
    }

    val pathColor = MaterialTheme.colorScheme.secondaryContainer
    val point1Color = MaterialTheme.colorScheme.primaryContainer

    Canvas(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        if (points.isEmpty()) return@Canvas

        // Normalize points to fit canvas
        val minLat = points.minOf { it.latitude }
        val maxLat = points.maxOf { it.latitude }
        val minLng = points.minOf { it.longitude }
        val maxLng = points.maxOf { it.longitude }

        val padding = 8.dp.toPx()
        val w = size.width - padding * 2
        val h = size.height - padding * 2

        val normalized = points.map { point ->
            Offset(
                x = padding + ((point.longitude - minLng) / (maxLng - minLng).coerceAtLeast(0.0001)).toFloat() * w,
                y = padding + (1f - ((point.latitude - minLat) / (maxLat - minLat).coerceAtLeast(
                    0.0001
                )).toFloat()) * h
            )
        }

        // Draw glow/shadow path
        val path = Path().apply {
            moveTo(normalized.first().x, normalized.first().y)
            normalized.drop(1).forEach { lineTo(it.x, it.y) }
        }

        drawPath(
            path, color = pathColor.copy(alpha = 0.15f),
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path, color = pathColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Start & end markers
        drawCircle(
            point1Color,
            radius = 3.dp.toPx(),
            center = normalized.first()
        )
        drawCircle(point1Color, radius = 3.dp.toPx(), center = normalized.last())
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
                        polylineRoute = "~lqi@mfnfSjA_BzCcDgAcDjAcB"
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
                        polylineRoute = "~lqi@mfnfSjA_BzCcDgAcDjAcB"
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
                        polylineRoute = "~lqi@mfnfSjA_BzCcDgAcDjAcB"
                    )
                ),
            ),
            onAction = {}
        )
    }
}