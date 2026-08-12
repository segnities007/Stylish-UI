package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * One label/value pair of a [StylishDescriptions].
 *
 * @property label The key text.
 * @property value The value text.
 */
public data class StylishDescriptionItem(
    public val label: String,
    public val value: String,
)

/**
 * A label/value detail grid — the web "Descriptions" pattern from Ant
 * Design.
 *
 * Renders [items] in rows of [columns] label/value pairs, each pair
 * taking one grid column (label above value). Use it for read-only
 * detail views (vehicle info, account settings, record details).
 *
 * @param items The label/value pairs, in display order.
 * @param modifier Modifier applied to the root surface.
 * @param columns Number of pairs per row. Defaults to 2.
 * @param labelStyle Typography of the labels. Defaults to
 *   [MaterialTheme.typography.labelSmall].
 * @param valueStyle Typography of the values. Defaults to
 *   [MaterialTheme.typography.bodyMedium].
 * @param labelColor Color of the labels. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param valueColor Color of the values. Defaults to
 *   [MaterialTheme.colorScheme.onSurface].
 * @param containerColor Background of the surface. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param shape Corner shape of the surface. Defaults to
 *   [RoundedCornerShape] with
 *   [DefaultStylishDimensions.connectedCornerRadius].
 * @param cellPadding Inner padding of each pair. Defaults to 16 x 12 dp.
 */
@Composable
public fun StylishDescriptions(
    items: List<StylishDescriptionItem>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    labelStyle: TextStyle = MaterialTheme.typography.labelSmall,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    labelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    shape: Shape = RoundedCornerShape(DefaultStylishDimensions.connectedCornerRadius),
    cellPadding: androidx.compose.foundation.layout.PaddingValues =
        androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 12.dp),
) {
    require(columns > 0) { "columns must be greater than zero" }

    Surface(
        shape = shape,
        color = containerColor,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            items.chunked(columns).forEachIndexed { rowIndex, rowItems ->
                if (rowIndex > 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing),
                ) {
                    rowItems.forEach { item ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(cellPadding),
                            verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing / 2),
                        ) {
                            Text(
                                item.label,
                                style = labelStyle,
                                color = labelColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                item.value,
                                style = valueStyle,
                                color = valueColor,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Stylish descriptions", showBackground = true, widthDp = 393)
@Composable
private fun StylishDescriptionsPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishDescriptions(
                items = listOf(
                    StylishDescriptionItem("車両名", "Stylish Car"),
                    StylishDescriptionItem("年式", "2026"),
                    StylishDescriptionItem("色", "ホワイト"),
                    StylishDescriptionItem("走行距離", "12,000 km"),
                ),
            )
        }
    }
}
