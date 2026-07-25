package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class StylishChartColors(val categorical: List<Color>)

fun ColorScheme.toStylishChartColors() = StylishChartColors(
    categorical = listOf(primary, tertiary, secondary, error, onSurfaceVariant, outline),
)

val MaterialTheme.stylishChartColors: StylishChartColors
    @Composable get() = colorScheme.toStylishChartColors()
