package com.segnities007.stylishui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * StylishUI の空間・輪郭・奥行きの基礎トークン。
 *
 * デフォルト値で StylishUI の標準ルック（Clear, Simple, Modern）を実現する。
 * [com.segnities007.stylishui.theme.StylishTheme] の `dimensions` パラメータで
 * グローバルに上書きできるほか、各コンポーネントのパラメータで個別に上書きできる。
 */
@Immutable
public data class StylishDimensions(
    /** Connected UIのアイテム間隔。 */
    public val connectedSpacing: Dp = 3.dp,
    /** Connected UIの輪郭線太さ。 */
    public val outlineWidth: Dp = 0.4.dp,
    /** タップ可能カードの浮き上がり標高。 */
    public val interactiveElevation: Dp = 1.dp,
    /** ヘッダー/FABの浮遊標高。 */
    public val floatingElevation: Dp = 2.dp,
    /** Connected UIの外側角丸半径。 */
    public val connectedCornerRadius: Dp = 12.dp,
    /** Connected UIの連結（内側）角丸半径。 */
    public val joinedCornerRadius: Dp = 2.dp,
    /** FAB/ヘッダーの角丸半径。 */
    public val floatingCornerRadius: Dp = 28.dp,
)

/** [StylishDimensions] のデフォルトインスタンス。非 Composable 文脈での既定値に使う。 */
public val DefaultStylishDimensions: StylishDimensions = StylishDimensions()

internal val LocalStylishDimensions: ProvidableCompositionLocal<StylishDimensions> =
    staticCompositionLocalOf { DefaultStylishDimensions }
