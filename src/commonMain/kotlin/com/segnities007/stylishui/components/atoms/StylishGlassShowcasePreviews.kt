package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.patterns.StylishModernScreen
import com.segnities007.stylishui.theme.StylishTheme

/**
 * ガラス表現のバリエーション確認用プレビュー集(公開 API は無し)。
 * 形状 / ティント濃度 / シーン / ボーダー / メディア背景+ディミング /
 * フローティング ファミリー実使用イメージ。
 */

private val MediaBackground = Brush.horizontalGradient(
    listOf(Color(0xFF7EC8E3), Color(0xFFB8E3C8), Color(0xFFF2D9A0)),
)

@Composable
private fun MediaBackdrop(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier.background(MediaBackground), contentAlignment = Alignment.Center) { content() }
}


/** ガラスの背後に置くカラフルなオブジェクト列(透明度確認用)。 */
@Composable
private fun BackdropObjects(modifier: Modifier = Modifier) {
    Row(
        modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(44.dp).background(Color(0xFFD7003A), CircleShape))
        Box(Modifier.size(44.dp).background(Color(0xFF165E83), RoundedCornerShape(10.dp)))
        Box(Modifier.size(44.dp).background(Color(0xFFC9A86A), CircleShape))
        Box(Modifier.size(44.dp).background(Color(0xFF6B8E3A), RoundedCornerShape(10.dp)))
        Text(
            "背景オブジェクト",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun GlassLabel(text: String, modifier: Modifier = Modifier) {
    Text(text, modifier = modifier.padding(horizontal = 20.dp, vertical = 12.dp))
}

// 1. 形状 ---------------------------------------------------------------

@Preview(name = "Glass / shapes", showBackground = true, widthDp = 340)
@Composable
private fun GlassShapesPreview() {
    StylishTheme(darkTheme = false) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GlassLabel("ピル")
            StylishGlassSurface(shape = RoundedCornerShape(50)) {
                GlassLabel("PagerIndicator 向き")
            }
            GlassLabel("角丸 24dp")
            StylishGlassSurface(shape = RoundedCornerShape(24.dp)) {
                GlassLabel("ヘッダー ピル向き")
            }
            GlassLabel("角丸 12dp")
            StylishGlassSurface {
                GlassLabel("カード向き")
            }
            GlassLabel("サークル")
            Box(Modifier.size(72.dp)) {
                StylishGlassSurface(shape = CircleShape, modifier = Modifier.size(72.dp)) {
                    Text("FAB", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

// 2. ティント濃度 ---------------------------------------------------------

@Preview(name = "Glass / tint strength on media", showBackground = true, widthDp = 340, heightDp = 420)
@Composable
private fun GlassTintStrengthPreview() {
    val tints = listOf(
        0.15f to "Clear 相当",
        0.3f to "薄め",
        0.55f to "Regular 相当",
        0.8f to "濃いめ",
    )
    MediaBackdrop(Modifier.padding(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            tints.forEach { (alpha, label) ->
                StylishGlassSurface(
                    variant = com.segnities007.stylishui.components.atoms.StylishGlassVariant.Clear,
                    tint = Color.White.copy(alpha = alpha),
                ) {
                    GlassLabel("$label (白 $alpha)")
                }
            }
            tints.forEach { (alpha, label) ->
                StylishGlassSurface(tint = Color.Black.copy(alpha = alpha * 0.6f)) {
                    GlassLabel("$label (黒 ${"%.2f".format(alpha * 0.6f)})")
                }
            }
        }
    }
}

// 3. シーン(反射) -------------------------------------------------------

@Preview(name = "Glass / sheen", showBackground = true, widthDp = 340, heightDp = 380)
@Composable
private fun GlassSheenPreview() {
    val sheens: List<Pair<Color?, String>> = listOf(
        null to "シーンなし",
        Color.White.copy(alpha = 0.15f) to "デフォルト",
        Color.White.copy(alpha = 0.35f) to "強め",
        Color.Black.copy(alpha = 0.12f) to "逆光(暗いシーン)",
    )
    StylishTheme(darkTheme = true) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            sheens.forEach { (sheen, label) ->
                StylishGlassSurface(sheen = sheen) {
                    GlassLabel(label)
                }
            }
        }
    }
}

// 4. ボーダー ------------------------------------------------------------

@Preview(name = "Glass / border", showBackground = true, widthDp = 340, heightDp = 380)
@Composable
private fun GlassBorderPreview() {
    val borders: List<Pair<Color?, String>> = listOf(
        Color.Transparent to "ボーダーなし",
        Color.White.copy(alpha = 0.22f) to "白ヘアライン(ダーク向け)",
        Color.Black.copy(alpha = 0.12f) to "黑ヘアライン(ライト向け)",
        Color.White.copy(alpha = 0.45f) to "強調白",
    )
    StylishMemoLikeBackdrop {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            borders.forEach { (color, label) ->
                Box {
                    StylishGlassSurface(borderColor = color) {
                        GlassLabel(label)
                    }
                }
            }
        }
    }
}

@Composable
private fun StylishMemoLikeBackdrop(content: @Composable () -> Unit) {
    // メモアプリのダーク背景+カードを模した背景
    Column(
        Modifier
            .background(Color(0xFF151614))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        content()
    }
}

// 5. フローティング ファミリー(実使用イメージ) -----------------------------

@Preview(name = "Glass / floating family on media", showBackground = true, widthDp = 340, heightDp = 560)
@Composable
private fun GlassFloatingFamilyPreview() {
    MediaBackdrop(Modifier.padding(16.dp)) {
        Box(Modifier.fillMaxWidth().height(520.dp)) {
            // ヘッダー ピル
            StylishGlassSurface(
                shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
                modifier = Modifier.align(Alignment.TopCenter),
            ) {
                GlassLabel("ヘッダー(タイトル+アクション)")
            }
            // FAB
            StylishGlassSurface(
                shape = CircleShape,
                interactive = true,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 64.dp),
            ) {
                Text("+", modifier = Modifier.align(Alignment.Center))
            }
            // ページ インジケーター
            StylishGlassSurface(
                shape = RoundedCornerShape(50),
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                GlassLabel("● ○")
            }
        }
    }
}


// 6. 和風ガラス -----------------------------------------------------------

@Preview(name = "Glass / Japanese / Sumi backdrop", showBackground = true, widthDp = 340, heightDp = 460)
@Composable
private fun JapaneseGlassSumiBackdropPreview() {
    // 墨色の背景に和風ガラスを並べる
    Column(
        Modifier
            .background(Color(0xFF141416))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        GlassLabel("和風ガラス — 墨背景")
        val presets = listOf(
            "墨 Sumi" to StylishJapaneseGlass.Sumi,
            "藍 Ai" to StylishJapaneseGlass.Ai,
            "抹茶 Matcha" to StylishJapaneseGlass.Matcha,
            "金箔 Kinpaku" to StylishJapaneseGlass.Kinpaku,
        )
        presets.forEach { (label, preset) ->
            Box {
                // 背後のオブジェクトが透けて見え、透明度が分かる
                BackdropObjects(Modifier.matchParentSize())
                StylishGlassSurface(preset = preset) {
                    GlassLabel(label)
                }
            }
        }
    }
}

@Preview(name = "Glass / Japanese / Washi backdrop", showBackground = true, widthDp = 340, heightDp = 460)
@Composable
private fun JapaneseGlassWashiBackdropPreview() {
    // 和紙風の明るい背景に淡色の和風ガラスを並べる
    Column(
        Modifier
            .background(Color(0xFFF8F5EE))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        GlassLabel("和風ガラス — 和紙背景")
        val presets = listOf(
            "桜 Sakura" to StylishJapaneseGlass.Sakura,
            "霞 Kasumi" to StylishJapaneseGlass.Kasumi,
            "水 Mizu" to StylishJapaneseGlass.Mizu,
            "紅 Beni" to StylishJapaneseGlass.Beni,
        )
        presets.forEach { (label, preset) ->
            Box {
                BackdropObjects(Modifier.matchParentSize())
                StylishGlassSurface(preset = preset) {
                    GlassLabel(label)
                }
            }
        }
    }
}
