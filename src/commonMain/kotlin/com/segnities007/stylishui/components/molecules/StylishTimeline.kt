package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * One entry of a [StylishTimeline].
 *
 * @property title Heading of the entry.
 * @property description Optional supporting text of the entry.
 * @property timestamp Optional time/date text shown above the title.
 */
public data class StylishTimelineItem(
    public val title: String,
    public val description: String = "",
    public val timestamp: String = "",
)

/**
 * A vertical list of chronological entries — the web "Timeline" pattern
 * from Ant Design and MUI (lab).
 *
 * Each entry is marked with a dot on a vertical line; the line connects
 * consecutive entries and ends at the last entry.
 *
 * @param items The entries in chronological order.
 * @param modifier Modifier applied to the root column.
 * @param dotSize Diameter of the entry dots. Defaults to 12 dp.
 * @param lineColor Color of the connecting line. Defaults to
 *   [MaterialTheme.colorScheme.outlineVariant].
 * @param dotColor Fill color of the entry dots. Defaults to
 *   [MaterialTheme.colorScheme.primary].
 * @param contentStyle Typography of [StylishTimelineItem.description].
 *   Defaults to [MaterialTheme.typography.bodyMedium].
 * @param titleStyle Typography of [StylishTimelineItem.title]. Defaults
 *   to [MaterialTheme.typography.titleSmall].
 */
@Composable
public fun StylishTimeline(
    items: List<StylishTimelineItem>,
    modifier: Modifier = Modifier,
    dotSize: Dp = 12.dp,
    lineColor: Color = MaterialTheme.colorScheme.outlineVariant,
    dotColor: Color = MaterialTheme.colorScheme.primary,
    contentStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->
            val isLast = index == items.lastIndex
            Row {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier
                            .size(dotSize)
                            .background(dotColor, CircleShape),
                    )
                    if (!isLast) {
                        Box(
                            Modifier
                                .width(2.dp)
                                .weight(1f)
                                .background(lineColor),
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.width(StylishTheme.dimensions.itemSpacing))
                Column(
                    modifier = Modifier
                        .padding(bottom = StylishTheme.dimensions.contentSpacing)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing / 2),
                ) {
                    if (item.timestamp.isNotEmpty()) {
                        Text(
                            item.timestamp,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        item.title,
                        style = titleStyle,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (item.description.isNotEmpty()) {
                        Text(
                            item.description,
                            style = contentStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Stylish timeline", showBackground = true, widthDp = 393)
@Composable
private fun StylishTimelinePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishTimeline(
                items = listOf(
                    StylishTimelineItem("オイル交換", "エンジンオイル交換", "2026/08/10"),
                    StylishTimelineItem("タイヤローテーション", "前後入れ替え", "2026/07/22"),
                    StylishTimelineItem("車検", "ユーザー車検にて合格", "2026/06/01"),
                ),
            )
        }
    }
}
