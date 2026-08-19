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
 * @property card The default container color for card surfaces. Derived from
 *   [ColorScheme.surfaceContainerLow] to provide a subtle elevation above the base surface.
 * @property cardContent The default content color (text, icons) rendered on top of a [card]
 *   container. Derived from [ColorScheme.onSurface].
 * @property button The default container color for filled buttons. Derived from
 *   [ColorScheme.primary] to match the primary action treatment.
 * @property buttonContent The default content color (text, icons) rendered on top of a
 *   [button] container. Derived from [ColorScheme.onPrimary].
 * @property chip The default container color for chips. Derived from
 *   [ColorScheme.surfaceContainerHigh] to distinguish chips from flat surfaces while keeping
 *   them visually lighter than cards.
 * @property chipContent The default content color (text, icons) rendered on top of a [chip]
 *   container. Derived from [ColorScheme.onSurfaceVariant].
 * @property textField The default container color for text fields. Derived from
 *   [ColorScheme.surfaceContainerHighest] to provide the deepest surface tone among input
 *   components, signalling interactivity.
 * @property textFieldContent The default content color (text, icons) rendered on top of a
 *   [textField] container. Derived from [ColorScheme.onSurface].
 * @see MaterialTheme.stylishComponentColors
 * @see StylishTheme
 */
@Immutable
public data class StylishComponentColors(
    public val groupedContainer: Color,
    public val card: Color,
    public val cardContent: Color,
    public val button: Color,
    public val buttonContent: Color,
    public val chip: Color,
    public val chipContent: Color,
    public val textField: Color,
    public val textFieldContent: Color,
)

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
        card = colorScheme.surfaceContainerLow,
        cardContent = colorScheme.onSurface,
        button = colorScheme.primary,
        buttonContent = colorScheme.onPrimary,
        chip = colorScheme.surfaceContainerHigh,
        chipContent = colorScheme.onSurfaceVariant,
        textField = colorScheme.surfaceContainerHighest,
        textFieldContent = colorScheme.onSurface,
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
