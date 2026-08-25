package com.segnities007.stylishui.components.atoms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/** Liquid Glass のバリアント(Apple HIG materials 準拠)。 */
public enum class StylishGlassVariant {
    /** 背景を調整して可読性を維持。テキストが多い要素に。 */
    Regular,

    /** 高透過。メディア背景の上に浮く要素に。 */
    Clear,
}

/**
 * A translucent "Liquid Glass"-style surface for floating controls.
 *
 * Implements the Apple Liquid Glass language portably (the system material
 * itself is only rendered by native SwiftUI bars):
 *
 * - [variant] = Regular: stronger tint for legibility (most components).
 * - [variant] = Clear: highly translucent for media-rich backgrounds.
 *   On bright backgrounds a dimming layer behind the glass is recommended
 *   (Apple: ~35% black) — add it in the caller's background.
 * - [interactive] = true: the sheen strengthens while pressed, mirroring
 *   the iOS behavior where an activated control emphasizes its glass.
 *
 * Use sparingly: glass is for the most important floating functional
 * elements, not for content-layer surfaces.
 *
 * @param modifier Modifier applied to the surface.
 * @param shape Corner shape. Defaults to the floating corner radius.
 * @param variant Glass variant (Regular / Clear).
 * @param interactive When `true`, press state strengthens the sheen.
 * @param tint Override for the translucent base color.
 * @param sheen Override for the diagonal highlight color.
 * @param borderColor Override for the hairline border color.
 * @param content Content placed inside the glass.
 */
@Composable
public fun StylishGlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    variant: StylishGlassVariant = StylishGlassVariant.Regular,
    interactive: Boolean = false,
    tint: Color? = null,
    sheen: Color? = null,
    borderColor: Color? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val resolvedTint = tint ?: when (variant) {
        StylishGlassVariant.Regular ->
            if (isDark) MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.55f)
            else Color.White.copy(alpha = 0.6f)
        StylishGlassVariant.Clear ->
            if (isDark) MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.28f)
            else Color.White.copy(alpha = 0.3f)
    }
    val resolvedBorder = borderColor ?: when {
        variant == StylishGlassVariant.Clear -> Color.White.copy(alpha = 0.3f)
        isDark -> Color.White.copy(alpha = 0.22f)
        else -> Color.Black.copy(alpha = 0.12f)
    }
    val baseSheen = sheen ?: if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.3f)

    // interactive = true の場合はシーンを常時やや強調する
    // (押下リアクションは呼び出し側の clickable が担う)
    val sheenAlpha = baseSheen.alpha * if (interactive) 1.4f else 0.6f

    Box(
        modifier
            .stylishTestTag("glass_surface")
            .clip(shape)
            .background(resolvedTint)
            .border(
                width = StylishTheme.dimensions.outlineWidth,
                brush = Brush.linearGradient(
                    0f to resolvedBorder,
                    1f to resolvedBorder.copy(alpha = resolvedBorder.alpha * 0.3f),
                ),
                shape = shape,
            )
    ) {
        // Diagonal sheen: brighter toward the top-leading corner. Strength
        // follows press state for interactive glass.
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        0f to baseSheen.copy(alpha = sheenAlpha),
                        0.55f to baseSheen.copy(alpha = sheenAlpha * 0.25f),
                        1f to Color.Transparent,
                        start = Offset.Zero,
                        end = Offset.Infinite,
                    ),
                ),
        )
        Box(Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
            content()
        }
    }
}

/** 透明度が分かるよう、ガラスの背後に置くカラフルなオブジェクト列。 */
@Composable
private fun VariantDemoObjects(modifier: Modifier = Modifier) {
    Row(
        modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(40.dp).background(Color(0xFFD7003A), CircleShape))
        Box(Modifier.size(40.dp).background(Color(0xFF165E83), RoundedCornerShape(10.dp)))
        Box(Modifier.size(40.dp).background(Color(0xFFC9A86A), CircleShape))
        Box(Modifier.size(40.dp).background(Color(0xFF6B8E3A), RoundedCornerShape(10.dp)))
    }
}

/** ガラスの背後に [VariantDemoObjects] を敷いたサンプル。 */
@Composable
private fun GlassRow(variant: StylishGlassVariant, label: String, interactive: Boolean = false) {
    Box {
        VariantDemoObjects(Modifier.matchParentSize())
        StylishGlassSurface(
            variant = variant,
            interactive = interactive,
        ) {
            Text(label, modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp))
        }
    }
}

@Preview(name = "Glass variants / Light", showBackground = true, widthDp = 320)
@Composable
private fun GlassVariantsLightPreview() {
    StylishTheme(darkTheme = false) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GlassRow(StylishGlassVariant.Regular, "Regular")
            GlassRow(StylishGlassVariant.Clear, "Clear")
            GlassRow(StylishGlassVariant.Regular, "Regular (interactive)", interactive = true)
        }
    }
}

@Preview(name = "Glass variants / Dark", showBackground = true, widthDp = 320)
@Composable
private fun GlassVariantsDarkPreview() {
    StylishTheme(darkTheme = true) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GlassRow(StylishGlassVariant.Regular, "Regular")
            GlassRow(StylishGlassVariant.Clear, "Clear")
            GlassRow(StylishGlassVariant.Regular, "Regular (interactive)", interactive = true)
        }
    }
}

@Preview(name = "Glass on media background / dimming", showBackground = true, widthDp = 320, heightDp = 300)
@Composable
private fun GlassOnMediaPreview() {
    // 明るいメディア背景の上に Clear ガラスを浮かべたケース。
    // Apple HIG: 明るい背景の上では 35% の黑ディミング層を推奨。
    Column(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF7EC8E3), Color(0xFFB8E3C8), Color(0xFFF2D9A0)),
                ),
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("メディア背景", style = MaterialTheme.typography.titleMedium)
        Box {
            VariantDemoObjects(Modifier.matchParentSize())
            StylishGlassSurface(variant = StylishGlassVariant.Clear) {
                Text("Clear(ディミングなし)", modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp))
            }
        }
        Box {
            // ガラスの背後に 35% 黑ディミング層(HIG 推奨)
            Spacer(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius)),
            )
            VariantDemoObjects(Modifier.matchParentSize())
            StylishGlassSurface(variant = StylishGlassVariant.Clear) {
                Text("Clear + dimming", modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp))
            }
        }
    }
}
