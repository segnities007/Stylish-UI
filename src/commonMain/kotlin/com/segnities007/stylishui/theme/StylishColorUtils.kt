package com.segnities007.stylishui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.materialkolor.ktx.harmonize

/**
 * Lightens this color by interpolating it toward [Color.White].
 *
 * A [factor] of `0.0f` returns the color unchanged; `1.0f` returns pure white. Values outside
 * the `0.0f..1.0f` range are clamped by the underlying [lerp] interpolation.
 *
 * @param factor The blend amount toward white, in the range `0.0f..1.0f`.
 * @return The lightened color.
 * @see stylishDarken
 */
public fun Color.stylishLighten(factor: Float): Color = lerp(this, Color.White, factor)

/**
 * Darkens this color by interpolating it toward [Color.Black].
 *
 * A [factor] of `0.0f` returns the color unchanged; `1.0f` returns pure black. Values outside
 * the `0.0f..1.0f` range are clamped by the underlying [lerp] interpolation.
 *
 * @param factor The blend amount toward black, in the range `0.0f..1.0f`.
 * @return The darkened color.
 * @see stylishLighten
 */
public fun Color.stylishDarken(factor: Float): Color = lerp(this, Color.Black, factor)

/**
 * Harmonizes this color with a [primary] color so that both share a compatible hue while
 * remaining distinguishable.
 *
 * Delegates to MaterialKolor's HCT-based harmonization ([com.materialkolor.ktx.harmonize]),
 * which shifts this color's hue toward the primary color's hue in a way that leaves the
 * original color recognizable. Use it to tint accent colors toward a brand primary without
 * losing their identity.
 *
 * @param primary The color to harmonize toward, typically `ColorScheme.primary`.
 * @return The harmonized color.
 */
public fun Color.stylishHarmonizeWith(primary: Color): Color = harmonize(primary)
