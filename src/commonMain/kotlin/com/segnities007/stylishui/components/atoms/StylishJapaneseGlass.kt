package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * 和風の伝統色に基づくガラス プリセット(ティント/ボーダー/シーンのセット)。
 *
 * [StylishGlassSurface] のオーバーロードに渡して使用する。
 * 色は日本の伝統色をベースに、ガラス用に半透明へ調整している。
 */
public data class StylishJapaneseGlass(
    public val tint: Color,
    public val border: Color,
    public val sheen: Color,
) {
    public companion object {
        /** 墨(すみ): 重厚な黒のガラス。ダーク背景で映える。 */
        public val Sumi: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0xE6121214),
            border = Color(0x59C9C9C9),
            sheen = Color(0x24FFFFFF),
        )

        /** 藍(あい): 深い藍色のガラス。 */
        public val Ai: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0xCC165E83),
            border = Color(0x66A0D8EF),
            sheen = Color(0x1FA0D8EF),
        )

        /** 抹茶(まっちゃ): 落ち着いた緑のガラス。 */
        public val Matcha: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0xB36B8E3A),
            border = Color(0x59C3D825),
            sheen = Color(0x1FC3D825),
        )

        /** 金箔(きんぱく): 金のガラス。ボーダーが強調される豪華な演出に。 */
        public val Kinpaku: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0x80C9A86A),
            border = Color(0x99E6C87A),
            sheen = Color(0x33FFE9A0),
        )

        /** 桜(さくら): 淡いピンクのガラス。 */
        public val Sakura: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0x80FEDFE1),
            border = Color(0x59E58DA5),
            sheen = Color(0x26FFFFFF),
        )

        /** 霞(かすみ): ほぼ透明な白のガラス。最も控えめ。 */
        public val Kasumi: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0x59DDE3E9),
            border = Color(0x40FFFFFF),
            sheen = Color(0x1FFFFFFF),
        )

        /** 水(みず): 淡い水色のガラス。 */
        public val Mizu: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0x59A0D8EF),
            border = Color(0x59FFFFFF),
            sheen = Color(0x1FA0D8EF),
        )

        /** 紅(べに): 深紋の赤のガラス。アクセント用。 */
        public val Beni: StylishJapaneseGlass = StylishJapaneseGlass(
            tint = Color(0x73D7003A),
            border = Color(0x66FF7F9E),
            sheen = Color(0x1FFF7F9E),
        )
    }
}

/**
 * 和風ガラス プリセットで [StylishGlassSurface] を描画するオーバーロード。
 *
 * @param preset 和風ガラス プリセット(墨/藍/抹茶/金箔/桜/霞/水/紅)。
 * @param modifier Modifier applied to the surface.
 * @param shape Corner shape. Defaults to the floating corner radius.
 * @param interactive When `true`, the sheen is emphasized.
 * @param content Content placed inside the glass.
 */
@Composable
public fun StylishGlassSurface(
    preset: StylishJapaneseGlass,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    interactive: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    StylishGlassSurface(
        modifier = modifier,
        shape = shape,
        // プリセットのティントを生かすため Clear ベースで上書き
        variant = StylishGlassVariant.Clear,
        interactive = interactive,
        tint = preset.tint,
        sheen = preset.sheen,
        borderColor = preset.border,
        content = content,
    )
}

@Preview(name = "Stylish japanese glass / Sumi", showBackground = true, widthDp = 300)
@Composable
private fun StylishJapaneseGlassSumiPreview() {
    StylishTheme(darkTheme = true) {
        Box(Modifier.padding(24.dp)) {
            StylishGlassSurface(preset = StylishJapaneseGlass.Sumi) {
                Text("墨 — 重厚な黒ガラス", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
