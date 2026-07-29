package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.foundation.ConnectedCorners
import com.segnities007.stylishui.foundation.ConnectedEdges
import com.segnities007.stylishui.foundation.connectedOutline
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * The default list-item renderer used by connected-list layouts when no custom
 * [ConnectedListItemContent] is supplied.
 *
 * This is the Finish-layer rendering: it dresses a row in the Stylish look —
 * grouped-container surface, interactive elevation, a hairline connected
 * outline — and wires tap/long-press interaction with haptic feedback. Items
 * whose [StylishConnectedListItem.onClick] and [StylishConnectedListItem.onLongClick]
 * are both `null`, or whose [StylishConnectedListItem.enabled] is `false`, are
 * rendered without elevation and do not respond to interaction. Actionable rows
 * are assigned `Role.Button` semantics; disabled rows are marked with the
 * `disabled()` semantic flag.
 *
 * @param item The [StylishConnectedListItem] data for the row.
 * @param modifier A modifier carrying layout constraints from the parent
 *   layout. Applied to the [Surface] root.
 * @param shape The connected [Shape] for this item's position in the group.
 * @param outlineEdges The [ConnectedEdges] indicating which sides draw outline
 *   borders.
 * @param outlineCorners The [ConnectedCorners] indicating which corners are
 *   rounded.
 * @param headlineMaxLines Maximum lines for the headline.
 * @param headlineOverflow Overflow strategy for the headline.
 * @param headlineStyle Text style for the headline.
 * @param supportingTextMaxLines Maximum lines for supporting text and lines.
 * @param supportingTextOverflow Overflow strategy for supporting text.
 * @param supportingTextStyle Text style for supporting text and lines.
 * @param containerColor Background color, or `null` for the grouped-container
 *   default.
 * @param contentColor Content color, or `null` for an enabled/disabled-aware
 *   default.
 * @param horizontalPadding Horizontal padding inside the row.
 * @param verticalPadding Vertical padding inside the row.
 *
 * @see StylishConnectedListItemRow
 * @see StylishConnectedListItemColumn
 * @see StylishConnectedListItemGrid
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun DefaultStylishConnectedListItem(
    item: StylishConnectedListItem,
    modifier: Modifier,
    shape: Shape,
    outlineEdges: ConnectedEdges,
    outlineCorners: ConnectedCorners,
    headlineMaxLines: Int,
    headlineOverflow: TextOverflow,
    headlineStyle: TextStyle,
    supportingTextMaxLines: Int,
    supportingTextOverflow: TextOverflow,
    supportingTextStyle: TextStyle,
    containerColor: Color?,
    contentColor: Color?,
    horizontalPadding: Dp,
    verticalPadding: Dp,
) {
    val haptic = LocalHapticFeedback.current
    val actionable = isActionable(
        enabled = item.enabled,
        hasClickAction = item.onClick != null,
        hasLongClickAction = item.onLongClick != null,
    )
    Surface(
        modifier = modifier
            .then(
                if (actionable) {
                    Modifier
                        .combinedClickable(
                            onClick = { item.onClick?.invoke() },
                            onLongClick = item.onLongClick?.let {
                                {
                                    haptic.performHapticFeedback(
                                        HapticFeedbackType.LongPress,
                                    )
                                    it()
                                }
                            },
                        )
                        .semantics { role = Role.Button }
                } else {
                    Modifier
                },
            )
            .semantics {
                if (!item.enabled) disabled()
            }
            .connectedOutline(outlineEdges, outlineCorners),
        shape = shape,
        color = containerColor ?: MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = contentColor ?: if (item.enabled) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurfaceVariant,
        shadowElevation = if (actionable) StylishTheme.dimensions.interactiveElevation else 0.dp,
    ) {
        if (item.content != null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            ) {
                item.content()
            }
        } else {
            Row(
                Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item.leadingContent?.invoke(this)
                Column(Modifier.weight(1f)) {
                    Text(
                        item.headline,
                        style = headlineStyle,
                        maxLines = headlineMaxLines,
                        overflow = headlineOverflow,
                    )
                    item.supportingText?.let {
                        Text(
                            it,
                            style = supportingTextStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = supportingTextMaxLines,
                            overflow = supportingTextOverflow,
                        )
                    }
                    item.supportingLines.forEach { line ->
                        Text(
                            line,
                            style = supportingTextStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = supportingTextMaxLines,
                            overflow = supportingTextOverflow,
                        )
                    }
                }
                item.trailingContent?.invoke(this)
            }
        }
    }
}

@Preview(name = "Stylish connected list item", showBackground = true, widthDp = 393)
@Composable
private fun DefaultStylishConnectedListItemPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            DefaultStylishConnectedListItem(
                item = StylishConnectedListItem("テーマ", supportingText = "システム設定", onClick = {}),
                modifier = Modifier,
                shape = connectedShape(ConnectedCorners.Standalone),
                outlineEdges = ConnectedEdges.All,
                outlineCorners = ConnectedCorners.Standalone,
                headlineMaxLines = Int.MAX_VALUE,
                headlineOverflow = TextOverflow.Ellipsis,
                headlineStyle = MaterialTheme.typography.titleMedium,
                supportingTextMaxLines = Int.MAX_VALUE,
                supportingTextOverflow = TextOverflow.Ellipsis,
                supportingTextStyle = MaterialTheme.typography.bodyMedium,
                containerColor = null,
                contentColor = null,
                horizontalPadding = 16.dp,
                verticalPadding = 14.dp,
            )
        }
    }
}
