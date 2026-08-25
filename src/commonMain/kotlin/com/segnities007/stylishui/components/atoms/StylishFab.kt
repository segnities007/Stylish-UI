package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.VisibilityState
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.isVisible
import com.segnities007.stylishui.foundation.rememberStylishInteractionSource
import com.segnities007.stylishui.foundation.stylishInteractiveSurface
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
 * Material FAB pressed behavior. Additionally, when [enabled], a
 * Material-style state layer (see [Modifier.stylishStateLayer]) darkens
 * the surface on hover and press, and a primary-colored focus ring (see
 * [Modifier.stylishFocusRing]) is drawn while the button holds keyboard
 * focus.
 *
 * ## Size precedence
 *
 * Two parameters control the button's diameter: [sizeVariant], which maps
 * a named size to the [DefaultStylishDimensions] `fab*Size` tokens, and
 * [size], which accepts an arbitrary exact diameter. When [size] is
 * non-null it always wins over [sizeVariant]; leave [size] `null` to use
 * a named variant. Prefer [sizeVariant] for the standard sizes and
 * [size] only for bespoke dimensions.
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_fab` for UI tests.
 * Callers can override it by passing their own `Modifier.testTag(...)`
 * in [modifier].
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
 * @param backdrop 磨りガラス用の背景コンテンツ。`null` 以外を渡すと FAB の
 *   円形面が [StylishFrostedGlassSurface] (すりガラス) になり、
 *   containerColor/border/tonalElevation は無視される。
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
    visibilityState: VisibilityState = VisibilityState.AlwaysVisible,
    backdrop: (@Composable BoxScope.() -> Unit)? = null,
) {
    val resolvedSize = size ?: when (sizeVariant) {
        StylishFabSize.Small -> StylishTheme.dimensions.fabSmallSize
        StylishFabSize.Regular -> StylishTheme.dimensions.fabSize
        StylishFabSize.Large -> StylishTheme.dimensions.fabLargeSize
    }
    val resolvedInteractionSource = rememberStylishInteractionSource(interactionSource)
    val resolvedShape = shape ?: CircleShape
    val isPressed by resolvedInteractionSource.collectIsPressedAsState()
    val reducedMotion = isStylishReducedMotionEnabled()
    val resolvedShadowElevation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else shadowElevation,
        animationSpec = if (reducedMotion) snap() else tween(StylishTheme.animation.durationShort),
        label = "fabShadowElevation",
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = visibilityState.isVisible(),
        enter = if (reducedMotion) fadeIn(snap()) else fadeIn(tween(StylishTheme.animation.durationShort)) + slideInVertically(tween(StylishTheme.animation.durationShort)) { it },
        exit = if (reducedMotion) fadeOut(snap()) else fadeOut(tween(StylishTheme.animation.durationShort)) + slideOutVertically(tween(StylishTheme.animation.durationShort)) { it },
    ) {
        Surface(
            modifier = Modifier
                .testTag("stylish_fab")
                .size(resolvedSize)
                .then(if (enabled) Modifier.stylishInteractiveSurface(resolvedInteractionSource, resolvedShape) else Modifier),
        shape = resolvedShape,
        color = if (backdrop != null) Color.Transparent else {
            containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh
        },
        contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
        border = if (backdrop != null) {
            null
        } else {
            border ?: BorderStroke(
                StylishTheme.dimensions.outlineWidth,
                MaterialTheme.colorScheme.outlineVariant,
            )
        },
        tonalElevation = if (backdrop != null) 0.dp else tonalElevation,
        shadowElevation = resolvedShadowElevation,
    ) {
        if (backdrop != null) {
            // 磨りガラス FAB: 円形のすりガラス面にアイコンを載せる。
            StylishFrostedGlassSurface(
                backdrop = backdrop,
                modifier = Modifier.size(resolvedSize),
                shape = resolvedShape,
            ) {
                IconButton(
                    onClick = onClick,
                    enabled = enabled,
                    interactionSource = resolvedInteractionSource,
                ) {
                    iconContent?.invoke() ?: Icon(imageVector, contentDescription)
                }
            }
        } else {
            IconButton(
                onClick = onClick,
                enabled = enabled,
                interactionSource = resolvedInteractionSource,
            ) {
                iconContent?.invoke() ?: Icon(imageVector, contentDescription)
            }
        }
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
