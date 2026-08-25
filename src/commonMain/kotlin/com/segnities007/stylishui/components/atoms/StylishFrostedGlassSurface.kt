package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * 磨りガラス(すりガラス)サーフェス。
 *
 * 2つのモードがある。
 *
 * - [backdrop] モード(静的): 渡された背景コンテンツを全面に描画し、その複製を
 *   [blur] でぼかす。背景が既知の静的コンテンツ向け。
 * - [glassState] モード(動的): [Modifier.stylishGlassSource] が録画した画面内容を
 *   自分の位置に合わせて再生してぼかす。スクロールする動的コンテンツにも追従する。
 *   こちらが優先され、Android 12 未満ではブラー無効のフォールバック表示になる。
 *
 * いずれも白濁ミルク層と粒子ノイズで磨り質感を追加する。デフォルトは
 * 「ほぼクリア」レシピ(haze=0.06 / blur=5dp / ティント 白2%)。
 *
 * @param backdrop 静的モード用の背景コンテンツ(BoxScope)。サーフェス全面に描画される。
 * @param glassState 動的モード用の [StylishGlassState]。backdrop より優先される。
 * @param modifier Modifier applied to the surface.
 * @param shape Corner shape. Defaults to the floating corner radius.
 * @param tint ガラスの着色。白系なら明るく、墨系なら暗い色味になる。
 * @param haze 白濁ミルク層の強さ。大きいほど乳白に曇る(すりガラス度)。
 * @param blurRadius 背景のぼかし半径。小さいほど背景が透ける。
 * @param content Content placed inside the glass.
 */
@Composable
public fun StylishFrostedGlassSurface(
    backdrop: (@Composable BoxScope.() -> Unit)? = null,
    glassState: StylishGlassState? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    tint: Color = Color.White.copy(alpha = 0.02f),
    haze: Float = 0.06f,
    blurRadius: Dp = 5.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val liveMode = glassState != null && isGlassBlurSupported()

    Box(modifier) {
        if (!liveMode) {
            // 0) 静的モード: 背景(そのまま)
            Box(Modifier.matchParentSize()) {
                backdrop?.invoke(this@Box)
            }

            // 1) 静的モード: 背景(ぼかし複製)。同じ領域に描くので位置ずれは起きない。
            Box(
                Modifier
                    .matchParentSize()
                    .clip(shape)
                    .blur(blurRadius),
            ) {
                backdrop?.invoke(this@Box)
            }
        } else {
            // 1') 動的モード: 録画済み背景を自分の位置に合わせて再生し、ぼかす。
            var effectPos by remember { mutableStateOf(Offset.Zero) }
            Box(
                Modifier
                    .matchParentSize()
                    .onGloballyPositioned { effectPos = it.positionInRoot() }
                    .clip(shape)
                    .blur(blurRadius)
                    .drawBehind {
                        // revision を読んで依存登録: ソースが再録画したら再描画される
                        glassState.revision.intValue
                        for (area in glassState.areas) {
                            val p = area.position.value
                            translate(p.x - effectPos.x, p.y - effectPos.y) {
                                area.layer?.let(::drawLayer)
                            }
                        }
                    },
            )
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
