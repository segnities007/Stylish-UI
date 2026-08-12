package com.segnities007.stylishui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Android actual: resolves wallpaper-derived dynamic schemes on API 31+ and returns `null`
 * on older runtimes where the dynamic APIs would throw.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun rememberDynamicColorSchemes(): Pair<ColorScheme, ColorScheme>? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return null
    val context = LocalContext.current
    return remember(context) {
        dynamicLightColorScheme(context) to dynamicDarkColorScheme(context)
    }
}
