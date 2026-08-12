package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/** iOS actual: no system wallpaper-derived scheme API; callers fall back to the static scheme. */
@Composable
internal actual fun rememberDynamicColorSchemes(): Pair<ColorScheme, ColorScheme>? = null
