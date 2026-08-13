package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * One entry of a [StylishBreadcrumb] navigation trail.
 *
 * @property label Display text of the item.
 * @property onClick Optional callback invoked when the item is tapped.
 *   When `null`, the item renders as static text (typically the current
 *   page — the last item).
 * @property icon Optional leading icon shown before [label].
 */
public data class StylishBreadcrumbItem(
    public val label: String,
    public val onClick: (() -> Unit)? = null,
    public val icon: (@Composable () -> Unit)? = null,
)

/**
 * A navigation trail showing the current location in a hierarchy — the
 * web "Breadcrumb" pattern from shadcn/ui, MUI, and Ant Design.
 *
 * Items are rendered left to right separated by [separator]. The last
 * item is rendered as static text in [activeColor]; earlier items are
 * clickable links. When [maxItems] is smaller than the item count, the
 * middle of the trail is collapsed into an ellipsis.
 *
 * @param items The breadcrumb trail, from root to current page.
 * @param modifier Modifier applied to the root row.
 * @param separator Visual separator between items. Defaults to a small
 *   right-arrow icon.
 * @param maxItems Maximum number of items to show before collapsing with
 *   an ellipsis. Defaults to [Int.MAX_VALUE] (never collapse).
 * @param textStyle Typography of the labels. Defaults to
 *   [MaterialTheme.typography.bodyMedium].
 * @param color Color of navigable (non-current) items. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param activeColor Color of the current (last) item. Defaults to
 *   [MaterialTheme.colorScheme.onSurface].
 */
@Composable
public fun StylishBreadcrumb(
    items: List<StylishBreadcrumbItem>,
    modifier: Modifier = Modifier,
    separator: @Composable () -> Unit = {
        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    },
    maxItems: Int = Int.MAX_VALUE,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    activeColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val visibleItems = if (items.size > maxItems && maxItems > 0) {
        buildList {
            addAll(items.take(maxItems / 2 + maxItems % 2))
            add(StylishBreadcrumbItem("…"))
            addAll(items.takeLast(maxItems / 2))
        }
    } else {
        items
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
    ) {
        visibleItems.forEachIndexed { index, item ->
            if (index > 0) separator()
            val isLast = index == visibleItems.lastIndex
            val clickable = item.onClick != null && !isLast
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
                modifier = Modifier
                    .semantics {
                        if (clickable) role = Role.Button
                    }
                    .then(
                        if (clickable) {
                            Modifier.clickable { item.onClick?.invoke() }
                        } else {
                            Modifier
                        },
                    ),
            ) {
                item.icon?.invoke()
                if (item.label == "…") {
                    Icon(
                        Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(18.dp),
                    )
                } else {
                    Text(
                        item.label,
                        style = textStyle,
                        color = if (isLast) activeColor else color,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Preview(name = "Stylish breadcrumb", showBackground = true, widthDp = 393)
@Composable
private fun StylishBreadcrumbPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishBreadcrumb(
                items = listOf(
                    StylishBreadcrumbItem("ホーム", onClick = {}),
                    StylishBreadcrumbItem("車両管理", onClick = {}),
                    StylishBreadcrumbItem("詳細"),
                ),
            )
        }
    }
}
