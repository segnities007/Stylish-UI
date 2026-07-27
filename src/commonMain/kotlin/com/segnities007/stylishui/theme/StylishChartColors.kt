package com.segnities007.stylishui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** チャート描画に使用するカテゴリ別カラーパレット。 */
@Immutable
public data class StylishChartColors(public val categorical: List<Color>)

/** [ColorScheme] からチャート用カラーを生成する。 */
public fun ColorScheme.toStylishChartColors(): StylishChartColors = StylishChartColors(
    categorical = listOf(primary, tertiary, secondary, error, onSurfaceVariant, outline),
)

/** 現在の [MaterialTheme] から取得できるチャート用カラー。 */
public val MaterialTheme.stylishChartColors: StylishChartColors
    @Composable get() = colorScheme.toStylishChartColors()
