package com.segnities007.stylishui.components.patterns

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.foundation.VisibilityState
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.isVisible
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A floating page header that hosts navigation, title, and action slots.
 *
 * Renders a rounded, elevated [Surface] bar pinned to the top of the
 * page with automatic status-bar inset padding. The [title] slot is
 * centered and tagged with [heading] semantics; [navigation] aligns to
 * the start edge and [actions] to the end edge. Typically placed inside
 * the `header` slot of [StylishPageContent] or the `topBar` slot of
 * [StylishScaffold].
 *
 * @param title Composable rendered at the horizontal center of the bar,
 *   usually a [Text] with the page title. Wrapped in a semantics
 *   [heading] node for accessibility.
 * @param navigation Optional composable rendered at the start (leading)
 *   edge, typically a [StylishIconButton] for back or drawer navigation.
 *   When null, no leading content is shown.
 * @param actions Optional composable rendered at the end (trailing) edge,
 *   typically one or more [StylishIconButton]s for search, settings, etc.
 *   When null, no trailing content is shown.
 * @param modifier Modifier applied to the outer [Column].
 * @param shape Corner shape of the header surface. Defaults to
 *   [RoundedCornerShape] with [StylishTheme.dimensions.floatingCornerRadius]
 *   radius, matching the floating aesthetic.
 * @param containerColor Background color of the header surface. Defaults
 *   to [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param contentColor Default content color propagated to child
 *   composables. Defaults to [MaterialTheme.colorScheme.onSurface].
 * @param border Optional [BorderStroke] drawn around the surface.
 *   Defaults to a stroke of [StylishTheme.dimensions.outlineWidth]
 *   (typically 0.4.dp) in
 *   [MaterialTheme.colorScheme.outlineVariant]. Pass null to remove.
 * @param tonalElevation Tonal elevation of the surface. Defaults to 4.dp.
 * @param shadowElevation Drop-shadow elevation of the surface. Defaults
 *   to [StylishTheme.dimensions.floatingElevation] (typically 2.dp).
 * @param height Fixed height of the inner content area. Defaults to 56.dp
 *   (standard app-bar height).
 * @param topPadding Space above the surface, after status-bar insets.
 *   Defaults to [StylishTheme.dimensions.itemSpacing].
 * @param bottomPadding Space below the surface, separating it from page
 *   content. Defaults to [StylishTheme.dimensions.contentSpacing].
 * @param actionsSpacing Horizontal gap between items inside the [actions]
 *   slot. Defaults to [StylishTheme.dimensions.inlineSpacing].
 * @param windowInsets [WindowInsets] consumed above the surface via
 *   [Modifier.windowInsetsPadding]. Defaults to zero — status-bar
 *   clearance is provided by the container (StylishScaffold /
 *   StylishModernScreen). Pass [WindowInsets.statusBars] only when the
 *   header is used WITHOUT such a container
 *   status area.
 *
 * @see StylishScaffold
 * @see StylishPageContent
 */
@Composable
public fun StylishHeader(
    title: @Composable () -> Unit,
    navigation: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.floatingCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = BorderStroke(
        StylishTheme.dimensions.outlineWidth,
        MaterialTheme.colorScheme.outlineVariant,
    ),
    tonalElevation: Dp = 4.dp,
    shadowElevation: Dp = StylishTheme.dimensions.floatingElevation,
    height: Dp = 56.dp,
    topPadding: Dp = StylishTheme.dimensions.itemSpacing,
    bottomPadding: Dp = StylishTheme.dimensions.contentSpacing,
    actionsSpacing: Dp = StylishTheme.dimensions.inlineSpacing,
    // Status-bar clearance is the CONTAINER's responsibility
    // (StylishScaffold / StylishModernScreen provide it). Pass a real
    // inset here only when the header is used without such a container.
    windowInsets: WindowInsets = WindowInsets(0.dp),
    visibilityState: VisibilityState = VisibilityState.AlwaysVisible,
) {
    val reducedMotion = isStylishReducedMotionEnabled()
    AnimatedVisibility(
        modifier = modifier,
        visible = visibilityState.isVisible(),
        enter = if (reducedMotion) fadeIn(snap()) else fadeIn(tween(StylishTheme.animation.durationShort)) + slideInVertically(tween(StylishTheme.animation.durationShort)) { -it },
        exit = if (reducedMotion) fadeOut(snap()) else fadeOut(tween(StylishTheme.animation.durationShort)) + slideOutVertically(tween(StylishTheme.animation.durationShort)) { -it },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .padding(top = topPadding, bottom = bottomPadding),
        ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            border = border,
            tonalElevation = tonalElevation,
            shadowElevation = shadowElevation,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                navigation?.let {
                    Box(
                        Modifier
                            .padding(start = StylishTheme.dimensions.inlineSpacing)
                    ) { it() }
                }
                // Title is constrained to the space BETWEEN navigation and
                // actions, so long text can never slide behind the buttons.
                // Center it within that space (M3 TopAppBar behavior).
                Box(
                    Modifier
                        .weight(1f)
                        .semantics { heading() },
                    contentAlignment = Alignment.Center,
                ) { title() }
                actions?.let {
                    Row(
                        Modifier
                            .padding(end = StylishTheme.dimensions.inlineSpacing),
                        horizontalArrangement = Arrangement.spacedBy(actionsSpacing),
                    ) { it() }
                }
            }
        }
    }
    }
}

@Preview(name = "Stylish header", showBackground = true, widthDp = 393)
@Composable
private fun StylishHeaderPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishHeader(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = { Text("車両一覧") },
                navigation = { StylishIconButton(Icons.Default.Search, "Navigation", {}) },
                actions = { StylishIconButton(Icons.Default.Search, "Search", {}) },
            )
        }
    }
}
