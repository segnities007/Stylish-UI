package com.segnities007.stylishui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spatial, outline, and elevation design tokens for the Stylish UI design system.
 *
 * The default values produce the standard Stylish look (Clear, Simple, Modern). Override
 * globally via the [com.segnities007.stylishui.theme.StylishTheme] composable's `dimensions`
 * parameter, or per-component through individual component parameters that accept [Dp] values.
 *
 * Access the active instance inside composables with
 * `StylishTheme.dimensions` (see [com.segnities007.stylishui.theme.StylishTheme]).
 *
 * @property connectedSpacing The gap between adjacent items in Connected UI layouts (connected
 *   cards, connected list items, connected chips, connected buttons). A small value (default
 *   3 dp) keeps items visually grouped while remaining individually distinguishable.
 * @property outlineWidth The stroke width of the hairline border drawn around Connected UI
 *   items via [com.segnities007.stylishui.foundation.connectedOutline]. The default 0.4 dp
 *   renders as a crisp 1-physical-pixel line on most densities.
 * @property interactiveElevation The tonal elevation applied to tappable cards and list items
 *   to signal interactivity. Default 1 dp produces a subtle lift without a heavy shadow.
 * @property floatingElevation The elevation for floating elements such as FABs, sticky headers,
 *   and bottom bars. Default 2 dp keeps them clearly above scrollable content.
 * @property connectedCornerRadius The outer corner radius for Connected UI items — the corners
 *   that face away from neighboring items (e.g. the top corners of the first item in a
 *   vertical list). Default 12 dp gives a soft, modern rounded-rectangle silhouette.
 * @property joinedCornerRadius The inner (junction) corner radius where two Connected UI items
 *   meet. Default 2 dp creates a tight notch that visually links adjacent items while
 *   preserving a slight separation cue.
 * @property floatingCornerRadius The corner radius for floating elements such as FABs and
 *   pill-shaped headers. Default 28 dp produces the fully-rounded capsule look typical of
 *   Material 3 extended FABs.
 * @see DefaultStylishDimensions
 * @see com.segnities007.stylishui.theme.StylishTheme
 */
@Immutable
public data class StylishDimensions(
    public val connectedSpacing: Dp = 3.dp,
    public val outlineWidth: Dp = 0.4.dp,
    public val interactiveElevation: Dp = 1.dp,
    public val floatingElevation: Dp = 2.dp,
    public val connectedCornerRadius: Dp = 12.dp,
    public val joinedCornerRadius: Dp = 2.dp,
    public val floatingCornerRadius: Dp = 28.dp,
)

/**
 * The default [StylishDimensions] instance with all tokens at their standard values.
 *
 * Used as the fallback in non-composable contexts (e.g. default parameter values in
 * [com.segnities007.stylishui.foundation.connectedShape]) and as the initial value of
 * [LocalStylishDimensions]. Inside a [com.segnities007.stylishui.theme.StylishTheme]
 * composition, prefer accessing tokens via `StylishTheme.dimensions`.
 *
 * @see StylishDimensions
 */
public val DefaultStylishDimensions: StylishDimensions = StylishDimensions()

internal val LocalStylishDimensions: ProvidableCompositionLocal<StylishDimensions> =
    staticCompositionLocalOf { DefaultStylishDimensions }
