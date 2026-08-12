package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * The predefined size of a [StylishFab].
 *
 * [Regular] matches the classic 56 dp FAB, [Small] the compact 40 dp
 * FAB, and [Large] the prominent 96 dp FAB. Sizes map to the
 * [DefaultStylishDimensions] `fab*Size` tokens.
 *
 * @see StylishFab
 */
public enum class StylishFabSize {
    /** Compact 40 dp FAB (maps to `fabSmallSize`). */
    Small,

    /** Classic 56 dp FAB (maps to `fabSize`). */
    Regular,

    /** Prominent 96 dp FAB (maps to `fabLargeSize`). */
    Large,
}

/**
 * A circular floating action button that floats above content with
 * tonal and shadow elevation (floatingElevation, 2 dp). Renders a
 * single icon inside a [CircleShape] surface with a hairline outline
 * border.
 *
 * Use for the primary action of a screen (e.g. "create new item").
 * When [iconContent] is provided it replaces the default [Icon]
 * rendered from [imageVector] and [contentDescription], allowing
 * arbitrary composable content such as an animated icon or a badge.
 * When [enabled] is `false`, the inner [IconButton] rejects clicks
 * and Material applies its disabled alpha.
 *
 * While pressed, the shadow elevation animates down to 0 dp (using
 * [StylishTheme.animation.durationShort]), matching the standard
 * Material FAB pressed behavior.
 *
 * @param imageVector Icon drawn inside the button when [iconContent]
 *   is `null`.
 * @param contentDescription Accessibility label for [imageVector].
 *   Defaults to `null` (no label); provide one whenever the action is
 *   not otherwise described on screen.
 * @param onClick Called when the button is tapped.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders with Material's disabled treatment.
 * @param containerColor Background color of the surface. Defaults to
 *   `MaterialTheme.colorScheme.surfaceContainerHigh`.
 * @param contentColor Default tint for content inside the surface.
 *   Defaults to `MaterialTheme.colorScheme.onSurface`.
 * @param shape Shape of the surface. Defaults to [CircleShape].
 * @param sizeVariant Predefined size of the button (see
 *   [StylishFabSize]). Ignored when [size] is non-null.
 * @param size Exact diameter of the circular surface in dp. When
 *   non-null, overrides [sizeVariant]. Defaults to `null`.
 * @param border Border stroke around the surface. Defaults to a
 *   hairline of [StylishTheme.dimensions.outlineWidth] (0.4 dp)
 *   using `MaterialTheme.colorScheme.outlineVariant`.
 * @param tonalElevation Tonal elevation of the surface. Defaults to
 *   [StylishTheme.dimensions.floatingElevation].
 * @param shadowElevation Shadow elevation of the surface when not
 *   pressed. Defaults to [StylishTheme.dimensions.floatingElevation];
 *   while pressed the shadow animates to 0 dp.
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions (and to
 *   drive the pressed-state shadow animation). When `null`, an
 *   internal one is remembered.
 * @param iconContent Optional slot that replaces the default [Icon].
 *   When `null` (default), [imageVector] and [contentDescription]
 *   are used instead.
 *
 * @see StylishIconButton
 * @see StylishRoundedIconButton
 * @see StylishFabSize
 */
@Composable
public fun StylishFab(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    sizeVariant: StylishFabSize = StylishFabSize.Regular,
    size: Dp? = null,
    border: BorderStroke? = null,
    tonalElevation: Dp = StylishTheme.dimensions.floatingElevation,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    interactionSource: MutableInteractionSource? = null,
    iconContent: (@Composable () -> Unit)? = null,
) {
    val resolvedSize = size ?: when (sizeVariant) {
        StylishFabSize.Small -> StylishTheme.dimensions.fabSmallSize
        StylishFabSize.Regular -> StylishTheme.dimensions.fabSize
        StylishFabSize.Large -> StylishTheme.dimensions.fabLargeSize
    }
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by resolvedInteractionSource.collectIsPressedAsState()
    val resolvedShadowElevation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else shadowElevation,
        animationSpec = tween(StylishTheme.animation.durationShort),
        label = "fabShadowElevation",
    )
    Surface(
        modifier = modifier.size(resolvedSize),
        shape = shape ?: CircleShape,
        color = containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        border = border ?: BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        ),
        tonalElevation = tonalElevation,
        shadowElevation = resolvedShadowElevation,
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = resolvedInteractionSource,
        ) {
            iconContent?.invoke() ?: Icon(imageVector, contentDescription)
        }
    }
}

@Preview(name = "FAB default", showBackground = true)
@Composable
private fun StylishFabPreview() {
    StylishTheme(darkTheme = false) {
        StylishFab(Icons.Default.Add, "追加", {})
    }
}

@Preview(name = "FAB disabled", showBackground = true)
@Composable
private fun StylishFabDisabledPreview() {
    StylishTheme(darkTheme = false) {
        StylishFab(
            imageVector = Icons.Default.Add,
            contentDescription = "追加",
            onClick = {},
            enabled = false,
        )
    }
}

@Preview(name = "FAB sizes", showBackground = true)
@Composable
private fun StylishFabSizesPreview() {
    StylishTheme(darkTheme = false) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            StylishFab(Icons.Default.Add, "小", {}, sizeVariant = StylishFabSize.Small)
            StylishFab(Icons.Default.Add, "標準", {})
            StylishFab(Icons.Default.Add, "大", {}, sizeVariant = StylishFabSize.Large)
        }
    }
}
