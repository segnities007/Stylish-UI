package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A floating page footer that hosts action slots at the bottom of the
 * screen. Mirrors [StylishHeader] in visual language — rounded,
 * elevated, with a hairline outline — and is designed to fill the
 * `bottomBar` slot of [StylishScaffold].
 *
 * The [content] slot is centered, [leadingContent] aligns to the start
 * edge, and [trailingContent] to the end edge. Automatic
 * navigation-bar inset padding is applied below the surface.
 *
 * @param content Composable rendered at the horizontal center of the
 *   bar, typically a primary action button or label.
 * @param modifier Modifier applied to the outer [Column].
 * @param leadingContent Optional composable rendered at the start
 *   (leading) edge. When null, no leading content is shown.
 * @param trailingContent Optional composable rendered at the end
 *   (trailing) edge. When null, no trailing content is shown.
 * @param shape Corner shape of the footer surface. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.floatingCornerRadius].
 * @param containerColor Background color of the footer surface.
 * @param contentColor Default content color propagated to children.
 * @param border Optional [BorderStroke] drawn around the surface.
 *   Defaults to a hairline stroke using
 *   [StylishTheme.dimensions.outlineWidth]. Pass null to remove.
 * @param tonalElevation Tonal elevation of the surface.
 * @param shadowElevation Drop-shadow elevation of the surface.
 *   Defaults to [StylishTheme.dimensions.floatingElevation].
 * @param height Fixed height of the inner content area. Defaults to
 *   56.dp (standard bar height).
 * @param topPadding Space above the surface, separating it from page
 *   content. Defaults to [StylishTheme.dimensions.contentSpacing].
 * @param bottomPadding Space below the surface, before navigation-bar
 *   insets. Defaults to [StylishTheme.dimensions.itemSpacing].
 * @param actionsSpacing Horizontal gap between items inside
 *   [leadingContent] and [trailingContent] when they contain multiple
 *   children. Defaults to [StylishTheme.dimensions.inlineSpacing].
 *
 * @see StylishHeader
 * @see StylishScaffold
 */
@Composable
public fun StylishFooter(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = BorderStroke(
        StylishTheme.dimensions.outlineWidth,
        MaterialTheme.colorScheme.outlineVariant,
    ),
    tonalElevation: Dp = 4.dp,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    height: Dp = 56.dp,
    topPadding: Dp = StylishTheme.dimensions.contentSpacing,
    bottomPadding: Dp = StylishTheme.dimensions.itemSpacing,
    actionsSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = bottomPadding)
            .navigationBarsPadding(),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            border = border,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                contentAlignment = Alignment.Center,
            ) {
                content()
                leadingContent?.let {
                    Row(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = StylishTheme.dimensions.inlineSpacing),
                        horizontalArrangement = Arrangement.spacedBy(actionsSpacing),
                    ) { it() }
                }
                trailingContent?.let {
                    Row(
                        Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = StylishTheme.dimensions.inlineSpacing),
                        horizontalArrangement = Arrangement.spacedBy(actionsSpacing),
                    ) { it() }
                }
            }
        }
    }
}

@Preview(name = "Stylish footer", showBackground = true, widthDp = 393)
@Composable
private fun StylishFooterPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishFooter(
                modifier = Modifier.padding(horizontal = 20.dp),
                content = { Text("フッター") },
                leadingContent = { StylishIconButton(Icons.Default.Add, "追加", {}) },
                trailingContent = { StylishIconButton(Icons.Default.Add, "設定", {}) },
            )
        }
    }
}