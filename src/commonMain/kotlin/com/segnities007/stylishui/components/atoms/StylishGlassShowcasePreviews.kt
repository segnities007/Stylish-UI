package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.math.roundToInt

/**
 * 磨りガラス(frosted glass)比較用プレビュー(公開 API は無し)。
 *
 * Compose にネイティブの backdrop blur は無いため、背景をガラス内部で
 * 再描画して [blur] でぼかす。さらに白濁ミルク層と粒子ノイズを重ねて
 * 磨りガラスの質感を作る。すりガラス度は [ShowGroundGlassBar] の
 * haze / blurRadius / tint で調整する。
 */

/** 背景の視覚要素(グラデーション+文字+図形)。ガラス内部の再描画にも使う。 */
@Composable
private fun ShowBackdropVisual(modifier: Modifier = Modifier) {
    Box(
        modifier.background(
            Brush.linearGradient(
                listOf(Color(0xFFD7003A), Color(0xFF7A3FE0), Color(0xFF165E83)),
            ),
        ),
    ) {
        Text(
            "背景文字 ABC 123",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
        )
        Text(
            "背景文字 ABC 123",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
        )
        Text(
            "背景文字 ABC 123",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp),
        )
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp, end = 26.dp)
                .height(46.dp)
                .background(Color(0xFFF2D9A0), RoundedCornerShape(50)),
        )
    }
}

@Composable
private fun ShowColumn(content: @Composable () -> Unit) {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        content()
    }
}

/**
 * 磨りガラスバー。
 *
 * @param label 表示ラベル。
 * @param tint ガラスのベース色(白系なら薄く、墨系なら濃い色味になる)。
 * @param haze 白濁ミルク層の強さ。大きいほど乳白色に寄る(すりガラス度)。
 * @param blurRadius 背景複製のぼかし半径。小さいほど背後が透ける。
 */
@Composable
private fun ShowGroundGlassBar(
    label: String,
    tint: Color,
    haze: Float,
    blurRadius: Dp,
) {
    val density = LocalDensity.current
    var parentPx by remember { mutableStateOf(IntSize.Zero) }
    var glassPos by remember { mutableStateOf(Offset.Zero) }
    val shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius)

    Box(
        Modifier
            .fillMaxWidth()
            .height(96.dp)
            .onGloballyPositioned { parentPx = it.size },
    ) {
        ShowBackdropVisual(Modifier.matchParentSize())
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(56.dp)
                .onGloballyPositioned { coords -> glassPos = coords.positionInParent() },
        ) {
            // 1) ぼかした背景の複製
            Box(
                Modifier
                    .matchParentSize()
                    .clip(shape)
                    .blur(blurRadius),
            ) {
                ShowBackdropVisual(
                    Modifier.layout { measurable, _ ->
                        val placeable = measurable.measure(
                            Constraints.fixed(parentPx.width, parentPx.height),
                        )
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                                -glassPos.x.roundToInt(),
                                -glassPos.y.roundToInt(),
                            )
                        }
                    },
                )
            }

            // 2) 白濁ミルク層 + 粒子ノイズ(磨り質感)
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

            // 6) ラベル
            Box(Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
                Text(label, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(name = "Ground glass / light", showBackground = true, widthDp = 400, heightDp = 450)
@Composable
private fun ShowGroundGlassLightPreview() {
    StylishTheme(darkTheme = false) {
        ShowColumn {
            ShowGroundGlassBar(
                label = "磨りガラス ごく薄め",
                tint = Color.White.copy(alpha = 0.03f),
                haze = 0.10f,
                blurRadius = 8.dp,
            )
            ShowGroundGlassBar(
                label = "ほぼクリア",
                tint = Color.White.copy(alpha = 0f),
                haze = 0.2f,
                blurRadius = 2.dp,
            )
        }
    }
}

@Preview(name = "Ground glass / dark", showBackground = true, widthDp = 400, heightDp = 450)
@Composable
private fun ShowGroundGlassDarkPreview() {
    StylishTheme(darkTheme = true) {
        ShowColumn {
            ShowGroundGlassBar(
                label = "磨りガラス ごく薄め",
                tint = Color.Black.copy(alpha = 0.2f),
                haze = 0.2f,
                blurRadius = 4.dp,
            )
            ShowGroundGlassBar(
                label = "ほぼクリア",
                tint = Color.White.copy(alpha = 0f),
                haze = 0.2f,
                blurRadius = 2.dp,
            )
        }
    }
}

@Preview(name = "Ground glass / tint compare light", showBackground = true, widthDp = 400, heightDp = 450)
@Composable
private fun ShowTintCompareLightPreview() {
    // haze は固定(0.16)。tint だけ変えると「着色」の効果が分かる。
    StylishTheme(darkTheme = false) {
        ShowColumn {
            ShowGroundGlassBar(
                label = "tint なし(白濁のみ)",
                tint = Color.Transparent,
                haze = 0.16f,
                blurRadius = 13.dp,
            )
            listOf(
                "tint 白" to Color.White.copy(alpha = 0.08f),
                "tint 墨" to StylishJapaneseGlass.Sumi.tint,
                "tint 藍" to StylishJapaneseGlass.Ai.tint,
                "tint 紅" to StylishJapaneseGlass.Beni.tint,
            ).forEach { (label, tint) ->
                ShowGroundGlassBar(
                    label = label,
                    tint = tint,
                    haze = 0.16f,
                    blurRadius = 13.dp,
                )
            }
        }
    }
}

@Preview(name = "Ground glass / tint compare dark", showBackground = true, widthDp = 400, heightDp = 450)
@Composable
private fun ShowTintCompareDarkPreview() {
    StylishTheme(darkTheme = true) {
        ShowColumn {
            ShowGroundGlassBar(
                label = "tint なし(白濁のみ)",
                tint = Color.Transparent,
                haze = 0.16f,
                blurRadius = 13.dp,
            )
            listOf(
                "tint 白" to Color.White.copy(alpha = 0.08f),
                "tint 墨" to StylishJapaneseGlass.Sumi.tint,
                "tint 藍" to StylishJapaneseGlass.Ai.tint,
                "tint 紅" to StylishJapaneseGlass.Beni.tint,
            ).forEach { (label, tint) ->
                ShowGroundGlassBar(
                    label = label,
                    tint = tint,
                    haze = 0.16f,
                    blurRadius = 13.dp,
                )
            }
        }
    }
}

// 実コンポーネント(StylishFrostedGlassSurface)のプレビュー ------------------

@Preview(name = "Frosted surface / light", showBackground = true, widthDp = 400, heightDp = 260)
@Composable
private fun ShowFrostedSurfaceLightPreview() {
    StylishTheme(darkTheme = false) {
        ShowColumn {
            StylishFrostedGlassSurface(
                backdrop = { ShowBackdropVisual(Modifier.matchParentSize()) },
                modifier = Modifier.fillMaxWidth().height(96.dp),
            ) {
                Text("ほぼクリア", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(name = "Frosted surface / dark", showBackground = true, widthDp = 400, heightDp = 260)
@Composable
private fun ShowFrostedSurfaceDarkPreview() {
    StylishTheme(darkTheme = true) {
        ShowColumn {
            StylishFrostedGlassSurface(
                backdrop = { ShowBackdropVisual(Modifier.matchParentSize()) },
                modifier = Modifier.fillMaxWidth().height(96.dp),
            ) {
                Text("ほぼクリア", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

// 磨りガラス FAB(実コンポーネント) -----------------------------------------

@Preview(name = "Frosted fab / light", showBackground = true, widthDp = 400, heightDp = 260)
@Composable
private fun ShowFrostedFabLightPreview() {
    StylishTheme(darkTheme = false) {
        ShowColumn {
            Box(Modifier.fillMaxWidth().height(96.dp)) {
                ShowBackdropVisual(Modifier.matchParentSize())
                Row(Modifier.align(Alignment.Center)) {
                    StylishFab(Icons.Default.Add, "追加", onClick = {}, backdrop = {
                        Box(Modifier.matchParentSize()) {
                            ShowBackdropVisual(Modifier.matchParentSize())
                        }
                    })
                }
            }
        }
    }
}

@Preview(name = "Frosted fab / dark", showBackground = true, widthDp = 400, heightDp = 260)
@Composable
private fun ShowFrostedFabDarkPreview() {
    StylishTheme(darkTheme = true) {
        ShowColumn {
            Box(Modifier.fillMaxWidth().height(96.dp)) {
                ShowBackdropVisual(Modifier.matchParentSize())
                Row(Modifier.align(Alignment.Center)) {
                    StylishFab(Icons.Default.Add, "追加", onClick = {}, backdrop = {
                        Box(Modifier.matchParentSize()) {
                            ShowBackdropVisual(Modifier.matchParentSize())
                        }
                    })
                }
            }
        }
    }
}

// 動的すりガラス(stylishGlassSource 録画方式) -------------------------------

@Preview(name = "Live ground glass / light", showBackground = true, widthDp = 400, heightDp = 420)
@Composable
private fun ShowLiveGlassLightPreview() {
    StylishTheme(darkTheme = false) {
        val state = remember { StylishGlassState() }
        Box(Modifier.fillMaxWidth().height(400.dp)) {
            // 背景(ソース): スクロールするリスト
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .stylishGlassSource(state),
            ) {
                repeat(12) { i ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier
                                .size(36.dp)
                                .background(
                                    listOf(Color(0xFFD7003A), Color(0xFF165E83), Color(0xFF6B8E3A))[i % 3],
                                    androidx.compose.foundation.shape.CircleShape,
                                ),
                        )
                        Text(
                            "アイテム " + i,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 14.dp),
                        )
                    }
                }
            }

            // フローティング ヘッダー(動的すりガラス)
            StylishFrostedGlassSurface(
                glassState = state,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                tint = Color.White.copy(alpha = 0.05f),
                haze = 0.16f,
                blurRadius = 13.dp,
            ) {
                Text(
                    "磨りガラス ヘッダー",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 14.dp),
                )
            }
        }
    }
}

@Preview(name = "Live ground glass / dark", showBackground = true, widthDp = 400, heightDp = 420)
@Composable
private fun ShowLiveGlassDarkPreview() {
    StylishTheme(darkTheme = true) {
        val state = remember { StylishGlassState() }
        Box(Modifier.fillMaxWidth().height(400.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .stylishGlassSource(state),
            ) {
                repeat(12) { i ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier
                                .size(36.dp)
                                .background(
                                    listOf(Color(0xFFD7003A), Color(0xFF165E83), Color(0xFF6B8E3A))[i % 3],
                                    androidx.compose.foundation.shape.CircleShape,
                                ),
                        )
                        Text(
                            "アイテム " + i,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 14.dp),
                        )
                    }
                }
            }

            StylishFrostedGlassSurface(
                glassState = state,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                tint = Color.White.copy(alpha = 0.05f),
                haze = 0.16f,
                blurRadius = 13.dp,
            ) {
                Text(
                    "磨りガラス ヘッダー",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 14.dp),
                )
            }
        }
    }
}
