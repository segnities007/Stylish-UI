package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A compact, slot-first toolbar for page actions and responsive layouts.
 *
 * The title area is intentionally independent from navigation and actions:
 * callers can provide [titleContent] for breadcrumbs, search, or a badge,
 * while [actions] can contain any number of icon buttons. On narrow screens
 * the title is ellipsized instead of pushing actions off-screen. The
 * component is a molecule rather than a navigation bar, so it can be used
 * inside cards, dialogs, and desktop panes as well as at the top of a page.
 *
 * @param title Optional plain-text title. Ignored when [titleContent] is set.
 * @param modifier Modifier applied to the surface.
 * @param subtitle Optional supporting text below [title].
 * @param titleStyle Typography for the title.
 * @param subtitleStyle Typography for the subtitle.
 * @param contentColor Color for title and slot content.
 * @param containerColor Toolbar background.
 * @param shape Optional outer shape; defaults to no clipping.
 * @param tonalElevation Tonal elevation for a surface toolbar.
 * @param shadowElevation Shadow elevation, useful for floating toolbars.
 * @param contentPadding Toolbar padding. Use [PaddingValues] to tune start,
 *   end, top, and bottom independently for platform-specific insets.
 * @param navigationContent Optional leading slot (back button, avatar, etc.).
 * @param titleContent Optional custom title slot. It receives no scope so it
 *   can be reused independently of the toolbar.
 * @param actions Trailing actions, receiving [RowScope] for weight/alignment.
 */
@Composable
public fun StylishToolbar(
    title: String? = null,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    subtitleStyle: TextStyle = MaterialTheme.typography.bodySmall,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = androidx.compose.ui.graphics.RectangleShape,
    tonalElevation: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = StylishTheme.dimensions.contentSpacing,
        vertical = StylishTheme.dimensions.contentSpacing / 1.5f,
    ),
    navigationContent: (@Composable () -> Unit)? = null,
    titleContent: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Surface(
        modifier = modifier.stylishTestTag("toolbar").fillMaxWidth(),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
        ) {
            navigationContent?.invoke()
            Box(modifier = Modifier.weight(1f)) {
                if (titleContent != null) {
                    titleContent()
                } else if (!title.isNullOrBlank()) {
                    Column(modifier = Modifier.semantics { heading() }) {
                        Text(
                            text = title,
                            style = titleStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                style = subtitleStyle,
                                color = contentColor.copy(alpha = 0.72f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
                verticalAlignment = Alignment.CenterVertically,
                content = actions,
            )
        }
    }
}

@Preview(name = "Stylish toolbar", showBackground = true, widthDp = 393)
@Composable
private fun StylishToolbarPreview() {
    StylishTheme(darkTheme = false) {
        StylishToolbar(
            title = "車両一覧",
            subtitle = "3台",
            modifier = Modifier.padding(20.dp),
            actions = { androidx.compose.material3.Text("編集") },
        )
    }
}
