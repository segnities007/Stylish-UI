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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.VisibilityState
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.isVisible
import com.segnities007.stylishui.foundation.stylishFocusRing
import com.segnities007.stylishui.foundation.stylishStateLayer
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A pill-shaped floating action button that pairs an icon with a text label,
 * floating above content with tonal and shadow elevation. Renders a horizontal
 * [Row] of icon + label inside a [RoundedCornerShape] surface with a hairline
 * outline border.
 *
 * Use for the primary action of a screen when the action benefits from a
 * descriptive label (e.g. "Compose", "Create New", "Add Item"). When
 * [iconContent] is provided it replaces the default [Icon] rendered from
 * [icon] and [contentDescription], allowing arbitrary composable content such
 * as an animated icon or a badge. When [enabled] is `false`, the button
 * rejects clicks and Material applies its disabled alpha.
 *
 * While pressed, the shadow elevation animates down to 0 dp (using
 * [StylishTheme.animation.durationShort]), matching the standard Material FAB
 * pressed behavior. Additionally, when [enabled], a Material-style state layer
 * (see [Modifier.stylishStateLayer]) darkens the surface on hover and press,
 * and a primary-colored focus ring (see [Modifier.stylishFocusRing]) is drawn
 * while the button holds keyboard focus.
 *
 * ## Testing
 *
 * The root carries the test tag `stylish_extended_fab` for UI tests. Callers
 * can override it by passing their own `Modifier.testTag(...)` in [modifier].
 *
 * @param text Label text displayed alongside the icon.
 * @param icon Icon drawn at the start of the button when [iconContent] is
 *   `null`.
 * @param contentDescription Accessibility label for [icon]. Defaults to
 *   `null` (no label); provide one whenever the action is not otherwise
 *   described on screen.
 * @param onClick Called when the button is tapped.
 * @param modifier [Modifier] applied to the root surface.
 * @param enabled When `false`, the button ignores pointer input and renders
 *   with Material's disabled treatment.
 * @param containerColor Background color of the surface. Defaults to
 *   `MaterialTheme.colorScheme.surfaceContainerHigh`.
 * @param contentColor Default tint for content inside the surface. Defaults
 *   to `MaterialTheme.colorScheme.onSurface`.
 * @param shape Shape of the surface. Defaults to a pill shape using
 *   [StylishTheme.dimensions.floatingCornerRadius].
 * @param border Border stroke around the surface. Defaults to a hairline of
 *   [StylishTheme.dimensions.outlineWidth] (0.4 dp) using
 *   `MaterialTheme.colorScheme.outlineVariant`.
 * @param tonalElevation Tonal elevation of the surface. Defaults to
 *   [StylishTheme.dimensions.floatingElevation].
 * @param shadowElevation Shadow elevation of the surface when not pressed.
 *   Defaults to [StylishTheme.dimensions.floatingElevation]; while pressed
 *   the shadow animates to 0 dp.
 * @param textStyle [TextStyle] applied to [text]. Defaults to
 *   `MaterialTheme.typography.labelLarge`.
 * @param interactionSource The [MutableInteractionSource] for the button,
 *   used to observe press/focus/hover interactions (and to drive the
 *   pressed-state shadow animation). When `null`, an internal one is
 *   remembered.
 * @param iconContent Optional slot that replaces the default [Icon]. When
 *   `null` (default), [icon] and [contentDescription] are used instead.
 * @param visibilityState Controls whether the button is visible or hidden
 *   (e.g. in response to scroll). Defaults to
 *   [VisibilityState.AlwaysVisible].
 *
 * @see StylishFab
 * @see VisibilityState
 */
@Composable
public fun StylishExtendedFab(
    text: String,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    border: BorderStroke? = null,
    tonalElevation: Dp = StylishTheme.dimensions.floatingElevation,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    interactionSource: MutableInteractionSource? = null,
    iconContent: (@Composable () -> Unit)? = null,
    visibilityState: VisibilityState = VisibilityState.AlwaysVisible,
) {
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by resolvedInteractionSource.collectIsPressedAsState()
    val reducedMotion = isStylishReducedMotionEnabled()
    val resolvedShadowElevation by animateDpAsState(
        targetValue = if (isPressed) 0.dp else shadowElevation,
        animationSpec = if (reducedMotion) snap() else tween(StylishTheme.animation.durationShort),
        label = "extendedFabShadowElevation",
    )
    AnimatedVisibility(
        visible = visibilityState.isVisible(),
        enter = if (reducedMotion) fadeIn(snap()) else fadeIn(tween(StylishTheme.animation.durationShort)) + slideInVertically(tween(StylishTheme.animation.durationShort)) { it },
        exit = if (reducedMotion) fadeOut(snap()) else fadeOut(tween(StylishTheme.animation.durationShort)) + slideOutVertically(tween(StylishTheme.animation.durationShort)) { it },
    ) {
        Surface(
            modifier = modifier
                .testTag("stylish_extended_fab")
                .heightIn(min = StylishTheme.dimensions.fabSize)
                .semantics { role = Role.Button }
                .then(
                    if (enabled) {
                        Modifier.stylishStateLayer(
                            interactionSource = resolvedInteractionSource,
                            shape = shape,
                        )
                    } else {
                        Modifier
                    },
                )
                .then(
                    if (enabled) {
                        Modifier.stylishFocusRing(
                            interactionSource = resolvedInteractionSource,
                            shape = shape,
                        )
                    } else {
                        Modifier
                    },
                ),
            shape = shape,
            color = containerColor ?: MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = contentColor ?: MaterialTheme.colorScheme.onSurface,
            border = border ?: BorderStroke(
                StylishTheme.dimensions.outlineWidth,
                MaterialTheme.colorScheme.outlineVariant,
            ),
            tonalElevation = tonalElevation,
            shadowElevation = resolvedShadowElevation,
            onClick = onClick,
            enabled = enabled,
            interactionSource = resolvedInteractionSource,
        ) {
            Row(
                modifier = Modifier.padding(
                    PaddingValues(
                        start = StylishTheme.dimensions.controlPadding,
                        end = StylishTheme.dimensions.controlPadding,
                    ),
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
            ) {
                iconContent?.invoke() ?: Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                )
                Text(
                    text = text,
                    style = textStyle,
                )
            }
        }
    }
}

@Preview(name = "Extended FAB default", showBackground = true)
@Composable
private fun StylishExtendedFabPreview() {
    StylishTheme(darkTheme = false) {
        StylishExtendedFab(
            text = "Compose",
            icon = Icons.Default.Edit,
            contentDescription = "Compose",
            onClick = {},
        )
    }
}

@Preview(name = "Extended FAB custom icon", showBackground = true)
@Composable
private fun StylishExtendedFabCustomIconPreview() {
    StylishTheme(darkTheme = false) {
        StylishExtendedFab(
            text = "Add Item",
            icon = Icons.Default.Add,
            contentDescription = "Add Item",
            onClick = {},
            iconContent = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
        )
    }
}

@Preview(name = "Extended FAB disabled", showBackground = true)
@Composable
private fun StylishExtendedFabDisabledPreview() {
    StylishTheme(darkTheme = false) {
        StylishExtendedFab(
            text = "Create New",
            icon = Icons.Default.Add,
            contentDescription = "Create New",
            onClick = {},
            enabled = false,
        )
    }
}

@Preview(name = "Extended FAB dark theme", showBackground = true)
@Composable
private fun StylishExtendedFabDarkPreview() {
    StylishTheme(darkTheme = true) {
        StylishExtendedFab(
            text = "Compose",
            icon = Icons.Default.Edit,
            contentDescription = "Compose",
            onClick = {},
        )
    }
}
