package com.gumrindelwald.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.gumrindelwald.designsystem.GradientBackground

@Composable
fun GumrunScaffold(
    withGradient: Boolean = true,
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = Color.Unspecified,
    content: @Composable (paddingValue: PaddingValues) -> Unit = {}
) {
    Scaffold(
        topBar = topAppBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier,
        containerColor = containerColor,
    ) { paddingValue ->
        if (withGradient) {
            GradientBackground {
                content(paddingValue)
            }
        } else {
            content(paddingValue)
        }
    }
}