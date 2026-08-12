package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A labeled metric display — the web "Statistic/Stat" pattern from Ant
 * Design and Chakra UI.
 *
 * Shows a [label] above a prominent [value], with an optional [delta]
 * indicator (up/down arrow) for changes.
 *
 * @param value The main metric text, e.g. "¥1,200,000".
 * @param modifier Modifier applied to the root column.
 * @param label Optional caption rendered above [value].
 * @param delta Optional change text, e.g. "+12.5%". When non-null, shown
 *   with a directional arrow.
 * @param deltaPositive Whether [delta] represents an improvement. When
 *   `true` the delta is tinted with [deltaColor]; otherwise it is tinted
 *   with the error color.
 * @param valueStyle Typography of [value]. Defaults to
 *   [MaterialTheme.typography.headlineMedium].
 * @param labelStyle Typography of [label]. Defaults to
 *   [MaterialTheme.typography.bodySmall].
 * @param deltaStyle Typography of [delta]. Defaults to
 *   [MaterialTheme.typography.bodySmall].
 * @param deltaColor Color of a positive [delta]. Defaults to
 *   [MaterialTheme.colorScheme.primary].
 * @param contentColor Color of [value]. Defaults to
 *   [MaterialTheme.colorScheme.onSurface].
 * @param horizontalAlignment Alignment of the column contents.
 *   Defaults to [Alignment.Start].
 */
@Composable
public fun StylishStatistic(
    value: String,
    modifier: Modifier = Modifier,
    label: String = "",
    delta: String? = null,
    deltaPositive: Boolean = true,
    valueStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    labelStyle: TextStyle = MaterialTheme.typography.bodySmall,
    deltaStyle: TextStyle = MaterialTheme.typography.bodySmall,
    deltaColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
    ) {
        if (label.isNotEmpty()) {
            Text(
                label,
                style = labelStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            value,
            style = valueStyle,
            color = contentColor,
        )
        if (delta != null) {
            val effectiveDeltaColor = if (deltaPositive) deltaColor else MaterialTheme.colorScheme.error
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing / 2),
            ) {
                Icon(
                    imageVector = if (deltaPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = effectiveDeltaColor,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    delta,
                    style = deltaStyle,
                    color = effectiveDeltaColor,
                )
            }
        }
    }
}

@Preview(name = "Stylish statistic", showBackground = true, widthDp = 393)
@Composable
private fun StylishStatisticPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                StylishStatistic(
                    label = "今月の燃費",
                    value = "15.2 km/L",
                    delta = "+1.3%",
                )
                StylishStatistic(
                    label = "メンテナンス費",
                    value = "¥32,000",
                    delta = "-8%",
                    deltaPositive = false,
                )
            }
        }
    }
}
