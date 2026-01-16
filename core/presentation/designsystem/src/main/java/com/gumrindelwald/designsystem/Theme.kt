package com.gumrindelwald.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val ThemeColorScheme = lightColorScheme(
    primary = GumAppPrimaryGreen,
    onPrimary = GumAppOnPrimary,
    primaryContainer = GumAppPrimaryContainer,
    onPrimaryContainer = GumAppOnPrimaryContainer,
    secondary = GumAppSecondaryGreen,
    onSecondary = GumAppOnSecondary,
    secondaryContainer = GumAppSecondaryContainer,
    onSecondaryContainer = GumAppOnSecondaryContainer,
    tertiary = GumAppTertiaryGreen,
    onTertiary = GumAppOnTertiary,
    tertiaryContainer = GumAppTertiaryContainer,
    onTertiaryContainer = GumAppOnTertiaryContainer,
    error = GumAppError,
    onError = GumAppOnError,
    errorContainer = GumAppErrorContainer,
    onErrorContainer = GumAppOnErrorContainer,
    surface = GumAppSurface,
    onSurface = GumAppOnSurface,
)

@Composable
fun GumAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = ThemeColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = AppTypography
    )
}