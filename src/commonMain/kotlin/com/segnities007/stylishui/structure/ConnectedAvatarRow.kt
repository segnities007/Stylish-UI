package com.segnities007.stylishui.structure

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A headless overlapping avatar row layout (Structure layer).
 *
 * Renders up to [maxVisible] avatars in a horizontal [Row] with negative
 * spacing so that each avatar overlaps the previous one by [overlap]. When
 * `items.size` exceeds [maxVisible], an overflow indicator is rendered as the
 * last element; the overflow indicator is produced by the [overflow] slot so
 * that callers control its appearance. Each visible avatar is produced by the
 * [avatar] slot, which receives the item, its index within the visible slice,
 * and a [Modifier] that encodes the overlap offset.
 *
 * This component makes **no** visual decisions — no colors, shapes, borders,
 * or typography. It is the headless backbone that the Stylish Finish
 * counterpart `StylishConnectedAvatarRow` consumes by supplying a styled
 * [avatar] and [overflow] renderer. Supply your own slots to render a custom
 * skin over the same overlapping geometry.
 *
 * @param T The type of each item in the row.
 * @param items The list of items to represent as avatars.
 * @param modifier [Modifier] applied to the root [Row].
 * @param maxVisible The maximum number of avatars to render before showing an
 *   overflow indicator. Defaults to `5`. Must be at least `1`.
 * @param overlap The horizontal offset applied between consecutive avatars.
 *   Negative values cause avatars to overlap; positive values produce gaps.
 *   Defaults to `(-8).dp`.
 * @param avatar A composable lambda that renders a single avatar. Receives the
 *   item, its index within the visible slice, and a [Modifier] that encodes
 *   the overlap offset and z-ordering.
 * @param overflow A composable lambda that renders the overflow indicator
 *   shown when `items.size > [maxVisible]`. Receives the number of hidden
 *   items and a [Modifier] that encodes the overlap offset. Defaults to an
 *   empty box.
 *
 * @see com.segnities007.stylishui.components.molecules.StylishConnectedAvatarRow
 */
@Composable
public fun <T> ConnectedAvatarRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 5,
    overlap: Dp = (-8).dp,
    avatar: @Composable (item: T, index: Int, modifier: Modifier) -> Unit,
    overflow: @Composable (hiddenCount: Int, modifier: Modifier) -> Unit = { _, _ -> },
) {
    require(maxVisible >= 1) { "maxVisible must be at least 1, was $maxVisible" }
    val visibleCount = minOf(items.size, maxVisible)
    val hasOverflow = items.size > maxVisible
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlap),
    ) {
        for (i in 0 until visibleCount) {
            avatar(items[i], i, Modifier)
        }
        if (hasOverflow) {
            overflow(items.size - maxVisible, Modifier)
        }
    }
}

@Preview(name = "Headless connected avatar row", showBackground = true, widthDp = 393)
@Composable
private fun ConnectedAvatarRowPreview() {
    StylishTheme(darkTheme = false) {
        Box(Modifier.padding(20.dp)) {
            ConnectedAvatarRow(
                items = listOf("AB", "CD", "EF", "GH", "IJ", "KL"),
                maxVisible = 4,
                overlap = (-12).dp,
                avatar = { item, _, itemModifier ->
                    Surface(
                        modifier = itemModifier.size(32.dp),
                        shape = CircleShape,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(item)
                        }
                    }
                },
                overflow = { hiddenCount, itemModifier ->
                    Surface(
                        modifier = itemModifier.size(32.dp),
                        shape = CircleShape,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("+$hiddenCount")
                        }
                    }
                },
            )
        }
    }
}
