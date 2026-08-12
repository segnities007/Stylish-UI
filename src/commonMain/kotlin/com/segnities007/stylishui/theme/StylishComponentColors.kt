package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance

/**
 * Derived component-level colors that are computed from the active [ColorScheme] rather than
 * stored as static palette values.
 *
 * These colors adapt automatically to light/dark themes because they are interpolated at
 * composition time from [ColorScheme.surface] and [ColorScheme.onSurface]. Obtain an instance
 * via [MaterialTheme.stylishComponentColors], which prefers the instance provided by the
 * enclosing [StylishTheme] (see its `componentColors` parameter) and falls back to computing
 * one from the active [ColorScheme].
 *
 * @property groupedContainer A subtle tinted surface used as the background for grouped or
 *   connected container layouts (e.g. connected card lists). In light themes this is a barely
 *   perceptible darkening of the surface (1.2% toward onSurface); in dark themes the shift is
 *   stronger (6%) to remain visible against dark backgrounds.
 * @see MaterialTheme.stylishComponentColors
 * @see StylishTheme
 */
@Immutable
public data class StylishComponentColors(public val groupedContainer: Color)

/**
 * The [StylishComponentColors] provided by the enclosing [StylishTheme], or `null` when no
 * [StylishTheme] (or one without an explicit `componentColors` value) is in scope. Read by
 * [MaterialTheme.stylishComponentColors], which falls back to a computed instance.
 */
internal val LocalStylishComponentColors: ProvidableCompositionLocal<StylishComponentColors?> =
    staticCompositionLocalOf { null }

/**
 * Computes [StylishComponentColors] from the given [colorScheme].
 *
 * The interpolation factor is chosen based on the background luminance: light backgrounds
 * use a 1.2% blend toward `onSurface`, while dark backgrounds use 6% so that the derived
 * container color remains distinguishable.
 *
 * @see StylishComponentColors
 */
internal fun stylishComponentColors(colorScheme: ColorScheme): StylishComponentColors =
    StylishComponentColors(
        groupedContainer = lerp(
            colorScheme.surface,
            colorScheme.onSurface,
            if (colorScheme.background.luminance() > 0.5f) 0.012f else 0.06f,
        ),
    )

/**
 * Returns the [StylishComponentColors] for the current theme.
 *
 * Prefers the instance provided by the enclosing [StylishTheme]'s `componentColors`
 * parameter (see [StylishTheme]). When none is provided, computes the colors from the
 * current [MaterialTheme.colorScheme] so the accessor works even without a [StylishTheme]
 * wrapper.
 *
 * @see StylishComponentColors
 * @see StylishTheme
 */
public val MaterialTheme.stylishComponentColors: StylishComponentColors
    @Composable get() = LocalStylishComponentColors.current ?: stylishComponentColors(colorScheme)
