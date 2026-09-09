package com.segnities007.stylishui.tokens

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Corner-radius shape tokens for the Stylish UI design system.
 *
 * The default values produce the standard Stylish look (Clear, Simple, Modern): a soft
 * small step for chips and badges, a medium step for cards and connected layouts, a large
 * step for dialogs and sheets, and a fully-rounded extra-large step for capsules and FABs.
 * Override globally via the [com.segnities007.stylishui.theme.StylishTheme] composable's
 * `shapes` parameter, or per-component through individual component parameters.
 *
 * Access the active instance inside composables with
 * `StylishTheme.shapes` (see [com.segnities007.stylishui.theme.StylishTheme]).
 *
 * @property small The smallest corner radius (6 dp): chips, badges, small controls.
 * @property medium The standard corner radius (12 dp): cards, connected items, inputs.
 * @property large The large corner radius (20 dp): dialogs, bottom sheets, menus.
 * @property extraLarge The fully-rounded corner radius (28 dp): capsules, extended FABs,
 *   floating headers.
 * @see DefaultStylishShapes
 * @see com.segnities007.stylishui.theme.StylishTheme
 */
@Immutable
public data class StylishShapes(
    /** Material-compatible extra-small shape used by compact menus and controls. */
    public val extraSmall: CornerBasedShape = RoundedCornerShape(4.dp),
    public val small: CornerBasedShape = RoundedCornerShape(6.dp),
    public val medium: CornerBasedShape = RoundedCornerShape(12.dp),
    public val large: CornerBasedShape = RoundedCornerShape(20.dp),
    public val extraLarge: CornerBasedShape = RoundedCornerShape(28.dp),
    /** Outer radius used by the Connected UI family. */
    public val connectedCornerRadius: Dp = 12.dp,
    /** Inner radius used where Connected UI items meet. */
    public val joinedCornerRadius: Dp = 2.dp,
    /** Radius used by floating bars, headers, and extended actions. */
    public val floatingCornerRadius: Dp = 28.dp,
)

/** Converts the single Stylish shape scale into the Material 3 shape scale used internally. */
public fun StylishShapes.toMaterialShapes(): androidx.compose.material3.Shapes =
    androidx.compose.material3.Shapes(
        extraSmall = extraSmall,
        small = small,
        medium = medium,
        large = large,
        extraLarge = extraLarge,
    )

/**
 * The default [StylishShapes] instance with all tokens at their standard values.
 *
 * Used as the initial value of [LocalStylishShapes]. Inside a
 * [com.segnities007.stylishui.theme.StylishTheme] composition, prefer accessing tokens via
 * `StylishTheme.shapes`.
 *
 * @see StylishShapes
 */
public val DefaultStylishShapes: StylishShapes = StylishShapes()

internal val LocalStylishShapes: ProvidableCompositionLocal<StylishShapes> =
    staticCompositionLocalOf { DefaultStylishShapes }
