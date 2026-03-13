package com.gumrindelwald.presentation.util

import androidx.compose.ui.graphics.Color
import com.gumrindelwald.domain.RunLocationTimestamp

data class PolylineUI(
    val loc1: RunLocationTimestamp,
    val loc2: RunLocationTimestamp,
    val color: Color
)
