package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance

/**
 * Derived component-level colors that are computed from the active [ColorScheme] rather than
 * stored as static palette values.
 *
 * These colors adapt automatically to light/dark themes because they are interpolated at
 * composition time from [ColorScheme.surface] and [ColorScheme.onSurface]. Obtain an instance
 * via [MaterialTheme.stylishComponentColors].
 *
 * @property groupedContainer A subtle tinted surface used as the background for grouped or
 *   connected container layouts (e.g. connected card lists). In light themes this is a barely
 *   perceptible darkening of the surface (1.2% toward onSurface); in dark themes the shift is
 *   stronger (6%) to remain visible against dark backgrounds.
 * @see MaterialTheme.stylishComponentColors
 */
@Immutable
public data class StylishComponentColors(public val groupedContainer: Color)

/**
 * Computes [StylishComponentColors] from the current [MaterialTheme.colorScheme].
 *
 * The interpolation factor is chosen based on the background luminance: light backgrounds
 * use a 1.2% blend toward `onSurface`, while dark backgrounds use 6% so that the derived
 * container color remains distinguishable.
 *
 * @see StylishComponentColors
 */
public val MaterialTheme.stylishComponentColors: StylishComponentColors
    @Composable get() {
        val scheme = colorScheme
        return StylishComponentColors(
            groupedContainer = lerp(
                scheme.surface,
                scheme.onSurface,
                if (scheme.background.luminance() > 0.5f) 0.012f else 0.06f,
            ),
        )
    }
