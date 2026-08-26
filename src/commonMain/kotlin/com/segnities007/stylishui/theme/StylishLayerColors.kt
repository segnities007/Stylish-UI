package com.segnities007.stylishui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Resolves a modal surface above the regular floating/card layer.
 * Light themes retain their bright surface. Dark themes use a higher,
 * lighter surface container so elevation never makes a layer darker.
 */
@Composable
internal fun stylishModalContainerColor(alpha: Float = 0.9f): Color {
    val scheme = MaterialTheme.colorScheme
    val base = if (scheme.background.luminance() > 0.5f) {
        scheme.surface
    } else {
        scheme.surfaceContainerHigh
    }
    return base.copy(alpha = alpha)
}

/**
 * Resolves an interactive surface placed on top of a floating container.
 * It stays unchanged in light mode and moves to a lighter container in dark mode.
 */
@Composable
internal fun stylishElevatedControlContainerColor(alpha: Float = 0.9f): Color {
    val scheme = MaterialTheme.colorScheme
    val base = if (scheme.background.luminance() > 0.5f) {
        scheme.surface
    } else {
        scheme.surfaceContainerHigh
    }
    return base.copy(alpha = alpha)
}


/**
 * Resolves cards above the page or modal that contains them.
 * Dark cards use the lightest container role; light cards keep the existing bright surface.
 */
@Composable
internal fun stylishCardContainerColor(alpha: Float = 0.9f): Color {
    val scheme = MaterialTheme.colorScheme
    val base = if (scheme.background.luminance() > 0.5f) {
        scheme.surface
    } else {
        scheme.surfaceContainerHighest
    }
    return base.copy(alpha = alpha)
}
