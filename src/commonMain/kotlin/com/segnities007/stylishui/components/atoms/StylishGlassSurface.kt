package com.segnities007.stylishui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A translucent "Liquid Glass"-style surface for floating controls.
 *
 * Pure Compose implementation, portable to every target: a translucent tint
 * with a diagonal sheen gradient and a hairline highlight border emulate the
 * glass look. Content scrolls or sits behind it and peeks through the tint.
 *
 * Note: the SYSTEM Liquid Glass material (iOS 26) is rendered by native
 * SwiftUI bars and cannot be provided by a Compose component. Use this
 * surface for the same visual language in fully Compose-driven UIs.
 *
 * @param modifier Modifier applied to the surface.
 * @param shape Corner shape of the glass. Defaults to the floating corner
 *   radius from the theme.
 * @param tint Translucent base color of the glass. Defaults to a raised
 *   surface tone at reduced alpha.
 * @param sheen Diagonal highlight gradient drawn over the tint. Defaults
 *   to a subtle white sheen from the top-leading corner.
 * @param borderColor Hairline border color. Defaults to a translucent
 *   white edge that catches light on both themes.
 * @param content Content placed inside the glass.
 */
@Composable
public fun StylishGlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    tint: Color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.55f),
    sheen: Color = Color.White.copy(alpha = 0.15f),
    borderColor: Color = Color.White.copy(alpha = 0.22f),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier
            .stylishTestTag("glass_surface")
            .clip(shape)
            .background(tint)
            .border(
                width = StylishTheme.dimensions.outlineWidth,
                brush = Brush.linearGradient(
                    0f to borderColor,
                    1f to borderColor.copy(alpha = borderColor.alpha * 0.3f),
                ),
                shape = shape,
            ),
    ) {
        // Diagonal sheen: brighter toward the top-leading corner.
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        0f to sheen,
                        0.55f to sheen.copy(alpha = sheen.alpha * 0.25f),
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
    StylishTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(Modifier.padding(24.dp)) {
                StylishGlassSurface {
                    Text(
                        "ガラス風サーフェス",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}
