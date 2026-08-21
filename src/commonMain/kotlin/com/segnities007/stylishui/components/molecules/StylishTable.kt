package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A simple read-only data table — the web "Table" pattern from shadcn/ui
 * and MUI.
 *
 * Renders [columns] as a header row and [rows] as body rows with equal
 * cell widths. Rows with fewer cells than columns are padded with empty
 * cells. For interactive, selectable rows prefer the connected list-item
 * family.
 *
 * @param columns The column headers.
 * @param rows The cell values; each row must not exceed [columns] in
 *   length.
 * @param modifier Modifier applied to the root surface.
 * @param containerColor Background of the table. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param headerContainerColor Background of the header row. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHighest].
 * @param contentColor Foreground color of the cells. Defaults to
 *   [MaterialTheme.colorScheme.onSurface].
 * @param headerTextStyle Typography of the header cells. Defaults to
 *   [MaterialTheme.typography.labelLarge].
 * @param cellTextStyle Typography of the body cells. Defaults to
 *   [MaterialTheme.typography.bodyMedium].
 * @param rowSpacing Vertical gap between body rows. Defaults to 0 dp.
 * @param cellPadding Inner padding of every cell. Defaults to
 *   to the theme's control padding tokens.
 * @param shape Corner shape of the table surface. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions].
 */
@Composable
public fun StylishTable(
    columns: List<String>,
    rows: List<List<String>>,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    headerContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    headerTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    cellTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    rowSpacing: Dp = 0.dp,
    cellPadding: PaddingValues = PaddingValues(
        horizontal = StylishTheme.dimensions.controlPadding,
        vertical = StylishTheme.dimensions.controlVerticalPadding,
    ),
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
) {
    Surface(
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(shape),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(headerContainerColor),
            ) {
                columns.forEach { header ->
                    Text(
                        header,
                        style = headerTextStyle,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(cellPadding),
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            rows.forEach { row ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = rowSpacing),
                ) {
                    repeat(columns.size) { index ->
                        Text(
                            row.getOrElse(index) { "" },
                            style = cellTextStyle,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(cellPadding),
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Stylish table", showBackground = true, widthDp = 393)
@Composable
private fun StylishTablePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishTable(
                columns = listOf("項目", "金額", "日付"),
                rows = listOf(
                    listOf("オイル交換", "¥12,000", "2026/08/10"),
                    listOf("タイヤ", "¥48,000", "2026/07/22"),
                    listOf("車検", "¥35,000", "2026/06/01"),
                ),
            )
        }
    }
}
