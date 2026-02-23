package com.gumrindelwald.designsystem


import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gumrindelwald.gumapp.core.presentation.designsystem.R

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    hasToolbar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthInPx = with(density) {
        configuration.screenWidthDp.dp.roundToPx()
    }

    val smallDimension = minOf(
        configuration.screenWidthDp.dp,
        configuration.screenHeightDp.dp
    )

    val smallDimensionInPx = with(density)
    {
        smallDimension.roundToPx()

    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val isAndroid12OrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S


    Box(
        modifier
            .fillMaxSize()
            .then(
                if (isAndroid12OrLater) {
                    Modifier.blur(smallDimension / 2)
                } else Modifier
            )
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isAndroid12OrLater) primaryColor else primaryColor.copy(alpha = .3f),
                            MaterialTheme.colorScheme.background
                        ),

                        center = Offset(
                            x = screenWidthInPx / 2f,
                            y = 300f
                        ),
                        radius = smallDimensionInPx / 2f
                    )
                )
        ) {
            GumrunLogoVertical()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (hasToolbar) {
                    Modifier
                } else {
                    Modifier.systemBarsPadding()
                }
            )
    ) {
        content()
    }
}

@Composable
fun GumrunLogoVertical(
    modifier: Modifier = Modifier
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.gumrun),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
