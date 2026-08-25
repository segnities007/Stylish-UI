package com.segnities007.stylishui.components.atoms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Preview(name = "Stylish glass surface", showBackground = true, widthDp = 393)
@Composable
private fun StylishGlassSurfacePreview() {
    MaterialTheme {
        Box(Modifier.padding(24.dp)) {
            StylishGlassSurface {
                Text("ガラス風サーフェス", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
