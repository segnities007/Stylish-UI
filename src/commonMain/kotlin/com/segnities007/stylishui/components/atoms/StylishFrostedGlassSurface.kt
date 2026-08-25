package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * 磨りガラス(すりガラス)サーフェス。
 *
 * Compose にはネイティブの backdrop blur が無いため、[backdrop] で渡された
 * 背景コンテンツをサーフェス自身が描画し、その複製を [blur] でぼかすことで
 * 「背後が透けて曇る」表現を作る。白濁ミルク層と粒子ノイズで磨り質感を追加する。
 *
 * デフォルト パラメータは「ほぼクリア」レシピ(haze=0.06 / blur=5dp /
 * ティント 白2%)。より曇らせる場合は [haze] と [blurRadius] を上げる。
 *
 * 注意: [backdrop] はこのサーフェスの全面を覆う形で描画されるため、
 * サーフェスの背後に見せたい内容と同じコンテンツを渡すこと。
 *
 * @param backdrop 背景コンテンツ(BoxScope)。サーフェス全面に描画され、
 *   そのぼかし複製も同じ内容から生成される。
 * @param modifier Modifier applied to the surface.
 * @param shape Corner shape. Defaults to the floating corner radius.
 * @param tint ガラスの着色。白系なら明るく、墨系なら暗い色味になる。
 * @param haze 白濁ミルク層の強さ。大きいほど乳白に曇る(すりガラス度)。
 * @param blurRadius 背景複製のぼかし半径。小さいほど背景が透ける。
 * @param content Content placed inside the glass.
 */
@Composable
public fun StylishFrostedGlassSurface(
    backdrop: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    tint: Color = Color.White.copy(alpha = 0.02f),
    haze: Float = 0.06f,
    blurRadius: Dp = 5.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier) {
        // 0) 背景(そのまま)
        Box(Modifier.matchParentSize()) {
            backdrop()
        }

        // 1) 背景(ぼかし複製)。同じ領域に描くので位置ずれは起きない。
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .blur(blurRadius),
        ) {
            backdrop()
        }

        // 2) 白濁ミルク層 + 粒子ノイズ(磨り質感)。粒子は固定シードで毎フレーム同一。
        Canvas(Modifier.matchParentSize().clip(shape)) {
            drawRect(Color.White.copy(alpha = haze))
            drawRect(Color.White.copy(alpha = haze * 0.4f))

            val rnd = kotlin.random.Random(20260825)
            repeat(600) { i ->
                drawCircle(
                    color = if (i % 2 == 0) {
                        Color.White.copy(alpha = 0.06f)
                    } else {
                        Color.Black.copy(alpha = 0.05f)
                    },
                    radius = 0.5f + rnd.nextFloat() * 0.9f,
                    center = Offset(rnd.nextFloat() * size.width, rnd.nextFloat() * size.height),
                )
            }
        }

        // 3) ティント
        Box(Modifier.matchParentSize().clip(shape).background(tint))

        // 4) シーン(左上からのハイライト)
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .background(
                    Brush.linearGradient(
                        0f to Color.White.copy(alpha = 0.30f),
                        0.45f to Color.Transparent,
                        start = Offset.Zero,
                        end = Offset.Infinite,
                    ),
                ),
        )

        // 5) ヘアライン ボーダー
        Box(
            Modifier
                .matchParentSize()
                .border(
                    width = StylishTheme.dimensions.outlineWidth,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0.12f)),
                    ),
                    shape = shape,
                ),
        )

        // 6) コンテンツ
        Box(Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
            content()
        }
    }
}
