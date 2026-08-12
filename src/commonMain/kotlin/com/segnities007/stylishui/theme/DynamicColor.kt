package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * Returns the dynamic (Material You) light and dark color schemes when the platform and
 * runtime support them, or `null` when they do not.
 *
 * On Android 12+ (API 31+) this resolves the wallpaper-derived schemes via
 * `dynamicLightColorScheme`/`dynamicDarkColorScheme`; on all other targets it returns
 * `null` and callers fall back to their static scheme.
 *
 * @return A pair of `(light, dark)` dynamic schemes, or `null` when unsupported.
 */
@Composable
internal expect fun rememberDynamicColorSchemes(): Pair<ColorScheme, ColorScheme>?
