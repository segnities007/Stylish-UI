package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A connected card that shares edges and corner radii with adjacent cards,
 * forming a visually continuous group. Supports tap and long-press
 * interactions; long-press triggers haptic feedback. When neither [onClick] nor
 * [onLongClick] is provided, the card renders flat (zero elevation)
 * and ignores pointer input.
 *
 * Use [StylishConnectedCardRow], [StylishConnectedCardColumn], or
 * [StylishConnectedCardGrid] to lay out multiple connected cards —
 * those layouts compute [outlineEdges] and [outlineCorners] automatically.
 *
 * @param title Primary text displayed in the card body.
 * @param supportingText Secondary text below the title.
 *   Omitted when blank.
 * @param onClick Called when the card is tapped. `null` (default) makes
 *   the card display-only.
 * @param onLongClick Called when the card is long-pressed. Triggers
 *   [HapticFeedbackType.LongPress] before invocation.
 * @param shape Shape of the card surface. Defaults to
 *   [connectedShape] with [ConnectedCorners.Standalone] (all corners
 *   use [DefaultStylishDimensions.connectedCornerRadius], a static
 *   constant of 12 dp — theme overrides do not affect this default).
 * @param outlineEdges Which edges of the hairline outline to draw.
 *   Defaults to [ConnectedEdges.All]. Layouts override this to
 *   suppress interior edges between adjacent cards.
 * @param outlineCorners Which corners use the large (outer) radius.
 *   Defaults to [ConnectedCorners.Standalone]. Layouts override this
 *   so only the group's exterior corners are rounded.
 * @param enabled When `false`, the card ignores pointer input and
 *   renders at zero elevation regardless of [onClick].
 * @param titleMaxLines Maximum lines for [title]. Defaults to 1.
 * @param titleOverflow Overflow strategy for [title].
 * @param titleStyle Text style for [title].
 * @param supportingTextMaxLines Maximum lines for [supportingText].
 *   Defaults to 1.
 * @param supportingTextOverflow Overflow strategy for [supportingText].
 * @param supportingTextStyle Text style for [supportingText].
 * @param containerColor Background color. Defaults to
 *   [stylishComponentColors.groupedContainer].
 * @param contentColor Default content color. Defaults to
 *   `MaterialTheme.colorScheme.onSurface`.
 * @param minHeight Minimum height of the card body. Defaults to 77.dp.
 * @param horizontalPadding Horizontal padding inside the card.
 *   Defaults to 16.dp.
 * @param verticalPadding Vertical padding inside the card.
 *   Defaults to 12.dp.
 * @param contentSpacing Horizontal gap between leading content, the text
 *   column, and trailing content. Defaults to
 *   [StylishTheme.dimensions.itemSpacing].
 * @param titleSpacing Vertical gap between [title] and [supportingText].
 *   Defaults to [StylishTheme.dimensions.inlineSpacing].
 * @param leadingContent Optional content before the text column
 *   (e.g. an icon or thumbnail). When `null` (default), no leading
 *   content is rendered and no spacing is reserved.
 * @param trailingContent Optional content after the text column
 *   (e.g. a chevron or badge). When `null` (default), no trailing
 *   content is rendered and no spacing is reserved.
 *
 * @see StylishConnectedCardRow
 * @see StylishConnectedCardColumn
 * @see StylishConnectedCardGrid
 * @see connectedShape
 * @see connectedOutline
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun StylishConnectedCard(
    title: String,
    modifier: Modifier = Modifier,
    supportingText: String = "",
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    shape: Shape = connectedShape(ConnectedCorners.Standalone),
    outlineEdges: ConnectedEdges = ConnectedEdges.All,
    outlineCorners: ConnectedCorners = ConnectedCorners.Standalone,
    enabled: Boolean = true,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    supportingTextMaxLines: Int = 1,
    supportingTextOverflow: TextOverflow = TextOverflow.Ellipsis,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    containerColor: Color? = null,
    contentColor: Color? = null,
    minHeight: Dp = 77.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 12.dp,
    contentSpacing: Dp = StylishTheme.dimensions.itemSpacing,
    titleSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val actionable = isActionable(
        enabled = enabled,
        hasClickAction = onClick != null,
        hasLongClickAction = onLongClick != null,
    )
    Card(
        modifier = modifier
            .connectedOutline(outlineEdges, outlineCorners)
            .then(
                if (actionable) {
                    Modifier
                        .semantics(mergeDescendants = true) { role = Role.Button }
                        .combinedClickable(
                            onClick = { onClick?.invoke() },
                            onLongClick = onLongClick?.let {
                                {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    it()
                                }
                            },
                        )
                } else {
                    Modifier
                },
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor ?: MaterialTheme.stylishComponentColors.groupedContainer,
            contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp,
        ),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = minHeight)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(contentSpacing),
        ) {
            leadingContent?.invoke()
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(titleSpacing, Alignment.CenterVertically),
            ) {
                Text(
                    title,
                    style = titleStyle,
                    maxLines = titleMaxLines,
                    overflow = titleOverflow,
                )
                if (supportingText.isNotBlank()) {
                    Text(
                        supportingText,
                        style = supportingTextStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = supportingTextMaxLines,
                        overflow = supportingTextOverflow,
                    )
                }
            }
            trailingContent?.invoke()
        }
    }
}

@Preview(name = "Stylish connected card", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedCardPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedCard(
                title = "項目のタイトル",
                supportingText = "補足テキスト",
                onClick = {},
                onLongClick = {},
                trailingContent = { Text("詳細", style = MaterialTheme.typography.labelSmall) },
            )
        }
    }
}
