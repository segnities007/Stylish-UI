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
 *   renders as a sub-pixel hairline that rounds to approximately 1 physical pixel on many device densities.
 * @property interactiveElevation The tonal elevation applied to tappable cards and list items
 *   to signal interactivity. Default 1 dp produces a subtle lift without a heavy shadow.
 * @property focusedElevation The elevation applied while an interactive element has keyboard or
 *   focus-ring focus. Default 1 dp matches [interactiveElevation] because the focus ring, not
 *   a higher shadow, is the primary focus cue.
 * @property hoveredElevation The elevation applied while a pointer hovers over an interactive
 *   element (pointer devices). Default 2 dp lifts the element slightly to acknowledge the
 *   hovered state without competing with floating surfaces.
 * @property pressedElevation The elevation applied while an interactive element is pressed.
 *   Default 0 dp presses the element flat, reinforcing the pressed feedback.
 * @property disabledElevation The elevation applied to disabled interactive elements. Default
 *   0 dp keeps disabled elements visually flat, matching the Material 3 disabled treatment.
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
 * @property spacingNone Zero spacing — no gap. Use when components should touch with no
 *   separation. Part of the semantic spacing scale.
 * @property spacingXxs Extra-extra-small spacing (2 dp): the finest visible gap, e.g. between a
 *   badge and its anchor. Part of the semantic spacing scale.
 * @property spacingXs Extra-small spacing (4 dp): gap between tightly related inline elements
 *   such as an icon and its label. Part of the semantic spacing scale.
 * @property spacingSm Small spacing (8 dp): gap between items within a group or list. Part of
 *   the semantic spacing scale.
 * @property spacingMd Medium-small spacing (12 dp): gap between related but distinct elements,
 *   e.g. a label and its associated control. Part of the semantic spacing scale.
 * @property spacingLg Large spacing (16 dp): gap between distinct content blocks within a
 *   section. Part of the semantic spacing scale.
 * @property spacingXl Extra-large spacing (20 dp): standard screen-edge padding and gap between
 *   major content regions. Part of the semantic spacing scale.
 * @property spacingXxl Extra-extra-large spacing (24 dp): gap between loosely related groups.
 *   Part of the semantic spacing scale.
 * @property spacingXxxl Extra-extra-extra-large spacing (32 dp): gap between top-level sections
 *   of a page. Part of the semantic spacing scale.
 * @property inlineSpacing The smallest spacing step (4 dp): gap between tightly related inline
 *   elements such as an icon and its label. Part of the Rhythm spacing scale (S6).
 * @property itemSpacing The small spacing step (8 dp): gap between items within a group or list.
 *   Part of the Rhythm spacing scale (S6).
 * @property contentSpacing The medium spacing step (16 dp): gap between distinct content blocks
 *   within a section. Part of the Rhythm spacing scale (S6).
 * @property sectionSpacing The large spacing step (32 dp): gap between top-level sections of a
 *   page. Part of the Rhythm spacing scale (S6).
 * @property buttonMinHeight The minimum height of primary buttons (including the connected
 *   button family). Default 52 dp matches the standard 44–56 dp touch-target guidance.
 * @property cardMinHeight The minimum height of cards and connected card items. Default 77 dp
 *   gives the compact card silhouette used across the design system.
 * @property iconButtonMinSize The minimum touch-target size for icon buttons. Default 48 dp
 *   matches Material touch-target guidance.
 * @property focusRingWidth The width of the shared keyboard-focus indicator. Default 2 dp keeps
 *   focus visible without changing layout geometry.
 * @property roundedIconButtonMinWidth The minimum width of rounded (label-carrying) icon
 *   buttons. Default 80 dp.
 * @property fabSize The size of the regular floating action button. Default 56 dp.
 * @property fabSmallSize The size of the small floating action button. Default 40 dp.
 * @property fabLargeSize The size of the large floating action button. Default 96 dp.
 * @property screenPadding The standard horizontal screen-edge padding for page-level layouts.
 *   Default 20 dp.
 * @property contentPadding The standard padding inside content areas (cards, dialogs, sheets).
 *   Default 16 dp.
 * @property controlPadding The standard horizontal padding inside controls (buttons, fields,
 *   cards). Default 16 dp.
 * @property controlVerticalPadding The standard vertical padding inside controls. Default 12 dp.
 * @property pieChartSize The default edge length of [com.segnities007.stylishui.components.charts.SimplePieChart].
 * @property barChartHeight The default height of [com.segnities007.stylishui.components.charts.SimpleBarChart].
 * @property lineChartHeight The default height of [com.segnities007.stylishui.components.charts.SimpleLineChart].
 * @see DefaultStylishDimensions
 * @see com.segnities007.stylishui.theme.StylishTheme
 */
@Immutable
public data class StylishDimensions(
    public val connectedSpacing: Dp = 3.dp,
    public val outlineWidth: Dp = 0.4.dp,
    public val interactiveElevation: Dp = 1.dp,
    public val focusedElevation: Dp = 1.dp,
    public val hoveredElevation: Dp = 2.dp,
    public val pressedElevation: Dp = 0.dp,
    public val disabledElevation: Dp = 0.dp,
    public val floatingElevation: Dp = 2.dp,
    public val connectedCornerRadius: Dp = 12.dp,
    public val joinedCornerRadius: Dp = 2.dp,
    public val floatingCornerRadius: Dp = 28.dp,
    public val spacingNone: Dp = 0.dp,
    public val spacingXxs: Dp = 2.dp,
    public val spacingXs: Dp = 4.dp,
    public val spacingSm: Dp = 8.dp,
    public val spacingMd: Dp = 12.dp,
    public val spacingLg: Dp = 16.dp,
    public val spacingXl: Dp = 20.dp,
    public val spacingXxl: Dp = 24.dp,
    public val spacingXxxl: Dp = 32.dp,
    public val inlineSpacing: Dp = 4.dp,
    public val itemSpacing: Dp = 8.dp,
    public val contentSpacing: Dp = 16.dp,
    public val sectionSpacing: Dp = 32.dp,
    public val buttonMinHeight: Dp = 52.dp,
    public val cardMinHeight: Dp = 77.dp,
    public val iconButtonMinSize: Dp = 48.dp,
    public val focusRingWidth: Dp = 2.dp,
    public val roundedIconButtonMinWidth: Dp = 80.dp,
    public val fabSize: Dp = 56.dp,
    public val fabSmallSize: Dp = 40.dp,
    public val fabLargeSize: Dp = 96.dp,
    public val screenPadding: Dp = 20.dp,
    public val contentPadding: Dp = 16.dp,
    public val controlPadding: Dp = 16.dp,
    public val controlVerticalPadding: Dp = 12.dp,
    public val pieChartSize: Dp = 160.dp,
    public val barChartHeight: Dp = 180.dp,
    public val lineChartHeight: Dp = 200.dp,
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
