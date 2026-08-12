package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/** JVM actual: dynamic color is not available; callers fall back to the static scheme. */
@Composable
internal actual fun rememberDynamicColorSchemes(): Pair<ColorScheme, ColorScheme>? = null
