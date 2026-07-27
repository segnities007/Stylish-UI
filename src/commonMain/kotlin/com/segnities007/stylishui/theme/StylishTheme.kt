package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.segnities007.stylishui.tokens.DefaultStylishDimensions
import com.segnities007.stylishui.tokens.LocalStylishDimensions
import com.segnities007.stylishui.tokens.StylishDimensions

/**
 * 現在の [StylishTheme] の値にアクセスするためのオブジェクト。
 *
 * `StylishTheme.dimensions` で、テーマが提供している [StylishDimensions] を取得できる。
 */
public object StylishTheme {
    /** 現在のテーマが提供するディメンショントークン。 */
    public val dimensions: StylishDimensions
        @Composable get() = LocalStylishDimensions.current
}

/** StylishUI共通テーマ。ライト/ダークの配色・タイポグラフィ・ディメンションを一括で適用する。 */
@Composable
public fun StylishTheme(
    darkTheme: Boolean,
    colorScheme: ColorScheme = if (darkTheme) StylishDarkColorScheme else StylishLightColorScheme,
    typography: Typography = StylishTypography,
    dimensions: StylishDimensions = DefaultStylishDimensions,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalStylishDimensions provides dimensions) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content,
        )
    }
}
