package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishAvatar
import com.segnities007.stylishui.structure.ConnectedAvatarRow
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Data class representing a single avatar in a [StylishConnectedAvatarRow].
 *
 * @property initials Short text shown inside the avatar when no image is
 *   provided, typically a person's initials (max 3 characters are displayed).
 * @property imageUrl Optional URL of an image to display inside the avatar.
 *   When `null`, the [initials] are shown instead. The image loading is
 *   delegated to the caller via the [StylishConnectedAvatarRow.avatar] slot;
 *   the default renderer ignores this field and shows [initials].
 */
@Immutable
public data class StylishAvatarItem(
    public val initials: String,
    public val imageUrl: String? = null,
)

/**
 * A horizontally overlapping row of circular avatars with an optional "+N"
 * overflow indicator, suited for showing team members, commenters, or
 * collaborators.
 *
 * This is the Finish-layer component: it supplies the Stylish avatar rendering
 * (circular [StylishAvatar] with a surface-colored border) and overflow
 * indicator to the headless Structure layout [ConnectedAvatarRow], which owns
 * arrangement and overlap geometry. Each avatar is drawn with a [border] that
 * visually separates it from its neighbours when they overlap. When
 * `items.size` exceeds [maxVisible], the overflow indicator shows the count of
 * hidden items as "+N" in a circle styled with [overflowContainerColor] and
 * [overflowContentColor]. The root carries a `contentDescription` of
 * `"N people"` for accessibility.
 *
 * @param items The list of [StylishAvatarItem] data objects that describe each
 *   avatar's initials and optional image URL.
 * @param modifier [Modifier] applied to the root row.
 * @param maxVisible The maximum number of avatars to render before showing an
 *   overflow indicator. Defaults to `5`.
 * @param size Diameter of each avatar and the overflow indicator. Defaults to
 *   `40.dp`.
 * @param overlap The horizontal offset applied between consecutive avatars.
 *   Negative values cause avatars to overlap; positive values produce gaps.
 *   Defaults to `(-8).dp`.
 * @param containerColor The background color of each avatar. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param contentColor The default content color (text/icon) inside each
 *   avatar. Defaults to [MaterialTheme.colorScheme.onSurface].
 * @param border The [BorderStroke] drawn around each avatar to visually
 *   separate it from its neighbours. Defaults to a 2 dp stroke in
 *   [MaterialTheme.colorScheme.surface].
 * @param overflowContainerColor The background color of the overflow
 *   indicator. Defaults to [MaterialTheme.colorScheme.primaryContainer].
 * @param overflowContentColor The content color of the overflow indicator.
 *   Defaults to [MaterialTheme.colorScheme.onPrimaryContainer].
 * @param groupContentDescription Produces a localized accessibility description for the group.
 *
 * @see ConnectedAvatarRow
 * @see StylishAvatar
 * @see StylishAvatarItem
 */
@Composable
public fun StylishConnectedAvatarRow(
    items: List<StylishAvatarItem>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 5,
    size: Dp = 40.dp,
    overlap: Dp = (-8).dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
    overflowContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    overflowContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    groupContentDescription: (Int) -> String = { count -> "$count people" },
) {
    val totalPeople = items.size
    ConnectedAvatarRow(
        items = items,
        modifier = modifier.semantics { contentDescription = groupContentDescription(totalPeople) },
        maxVisible = maxVisible,
        overlap = overlap,
        avatar = { item, _, itemModifier ->
            Surface(
                modifier = itemModifier.size(size),
                shape = CircleShape,
                border = border,
            ) {
                StylishAvatar(
                    initials = item.initials,
                    size = size,
                    containerColor = containerColor,
                    contentColor = contentColor,
                )
            }
        },
        overflow = { hiddenCount, itemModifier ->
            Surface(
                modifier = itemModifier.size(size),
                shape = CircleShape,
                color = overflowContainerColor,
                contentColor = overflowContentColor,
                border = border,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "+$hiddenCount",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        },
    )
}

@Preview(name = "Connected avatar row — 3 items", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedAvatarRowThreeItemsPreview() {
    StylishTheme(darkTheme = false) {
        Box(Modifier.padding(20.dp)) {
            StylishConnectedAvatarRow(
                items = listOf(
                    StylishAvatarItem("AB"),
                    StylishAvatarItem("CD"),
                    StylishAvatarItem("EF"),
                ),
            )
        }
    }
}

@Preview(name = "Connected avatar row — overflow", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedAvatarRowOverflowPreview() {
    StylishTheme(darkTheme = false) {
        Box(Modifier.padding(20.dp)) {
            StylishConnectedAvatarRow(
                items = listOf(
                    StylishAvatarItem("AB"),
                    StylishAvatarItem("CD"),
                    StylishAvatarItem("EF"),
                    StylishAvatarItem("GH"),
                    StylishAvatarItem("IJ"),
                    StylishAvatarItem("KL"),
                    StylishAvatarItem("MN"),
                ),
                maxVisible = 5,
            )
        }
    }
}

@Preview(name = "Connected avatar row — small size", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedAvatarRowSmallPreview() {
    StylishTheme(darkTheme = false) {
        Box(Modifier.padding(20.dp)) {
            StylishConnectedAvatarRow(
                items = listOf(
                    StylishAvatarItem("AB"),
                    StylishAvatarItem("CD"),
                    StylishAvatarItem("EF"),
                    StylishAvatarItem("GH"),
                ),
                size = 28.dp,
                overlap = (-6).dp,
            )
        }
    }
}
