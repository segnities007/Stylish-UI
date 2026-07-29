package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A shimmering placeholder line used to indicate loading content.
 * Renders a rounded rectangle whose opacity oscillates between
 * [minAlpha] and [maxAlpha] to produce a shimmer effect.
 *
 * @param modifier Modifier applied to the placeholder box. Use
 *   [Modifier.width] and [Modifier.height] to size it.
 * @param color Base color of the placeholder. Defaults to
 *   [MaterialTheme.colorScheme.surfaceVariant].
 * @param shape Corner shape. Defaults to
 *   [RoundedCornerShape] with 4.dp.
 * @param minAlpha Minimum opacity during the shimmer cycle.
 * @param maxAlpha Maximum opacity during the shimmer cycle.
 *
 * @see StylishSkeletonCard
 * @see StylishSkeletonAvatar
 */
@Composable
public fun StylishSkeletonLine(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = RoundedCornerShape(4.dp),
    minAlpha: Float = 0.3f,
    maxAlpha: Float = 0.7f,
) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "skeletonAlpha",
    )
    Box(
        modifier
            .clip(shape)
            .background(color.copy(alpha = alpha)),
    )
}

/**
 * A circular shimmering placeholder representing an avatar or icon.
 *
 * @param size Diameter of the circle. Defaults to 48.dp.
 * @param modifier Modifier applied to the placeholder box.
 * @param color Base color. Defaults to
 *   [MaterialTheme.colorScheme.surfaceVariant].
 *
 * @see StylishSkeletonLine
 * @see StylishSkeletonCard
 */
@Composable
public fun StylishSkeletonAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    StylishSkeletonLine(
        modifier = modifier.size(size),
        color = color,
        shape = CircleShape,
    )
}

/**
 * A card-shaped skeleton placeholder with an avatar, a title line,
 * and a body line, mimicking the layout of
 * [com.segnities007.stylishui.components.atoms.StylishConnectedCard].
 *
 * @param modifier Modifier applied to the outer [Column].
 * @param color Base color for all placeholder elements.
 *
 * @see StylishSkeletonLine
 * @see StylishSkeletonAvatar
 */
@Composable
public fun StylishSkeletonCard(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    Column(
        modifier = modifier.padding(
            horizontal = StylishTheme.dimensions.contentSpacing,
            vertical = StylishTheme.dimensions.itemSpacing,
        ),
    ) {
        Row {
            StylishSkeletonAvatar(color = color)
            Spacer(Modifier.width(StylishTheme.dimensions.itemSpacing))
            Column {
                StylishSkeletonLine(
                    Modifier
                        .width(160.dp)
                        .height(16.dp),
                    color = color,
                )
                Spacer(Modifier.height(StylishTheme.dimensions.inlineSpacing))
                StylishSkeletonLine(
                    Modifier
                        .width(100.dp)
                        .height(12.dp),
                    color = color,
                )
            }
        }
    }
}

@Preview(name = "Skeleton card", showBackground = true, widthDp = 393)
@Composable
private fun StylishSkeletonCardPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishSkeletonCard(Modifier.fillMaxWidth())
        }
    }
}