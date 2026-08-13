package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * The direction in which a [StylishSpeedDial] fans out its actions.
 *
 * [Up] and [Down] expand the actions vertically (above or below the main
 * button); [Start] and [End] expand them horizontally (before or after
 * the main button, respecting the layout direction).
 *
 * @see StylishSpeedDial
 */
public enum class SpeedDialDirection {
    /** Actions expand vertically above the main button. */
    Up,

    /** Actions expand vertically below the main button. */
    Down,

    /** Actions expand horizontally before (start of) the main button. */
    Start,

    /** Actions expand horizontally after (end of) the main button. */
    End,
}

/**
 * The scope in which each [StylishSpeedDial] action is composed.
 *
 * Receivers of the `actions` slot lambda use [index] to know which item
 * they are rendering and [expanded] to react to the dial's state (e.g.
 * swap an icon while open).
 *
 * @see StylishSpeedDial
 */
public interface SpeedDialScope {
    /** The zero-based position of this action among the dial's actions. */
    public val index: Int

    /** Whether the dial is currently expanded. */
    public val expanded: Boolean
}

private class StylishSpeedDialScope(
    override val index: Int,
    override val expanded: Boolean,
) : SpeedDialScope

/**
 * A floating action button that expands a fan of actions around itself
 * when tapped, mirroring Material UI's `SpeedDial`.
 *
 * The component is a compact stateful shell: [expanded] and
 * [onExpandedChange] are hoisted, and the main button (the existing
 * [StylishFab] atom) toggles the state. [actionCount] actions are
 * composed via [actions], each receiving its [SpeedDialScope.index];
 * render each action as its own small button (e.g. a circular
 * [androidx.compose.material3.Surface] with an icon) and call
 * [onActionClick] with the index from its click handler.
 *
 * The actions fan out along [direction] (up/down/start/end) with a
 * fade + expand/shrink animation driven by
 * [StylishTheme.animation.durationShort]. When the platform requests
 * reduced motion (see [isStylishReducedMotionEnabled]) the actions snap
 * in and out without animation, and the container does not animate its
 * size.
 *
 * ## Usage pattern
 *
 * Anchor the dial in the corner of a [Box] (typically bottom-end) and
 * let it float above the content:
 *
 * ```kotlin
 * Box(Modifier.fillMaxSize()) {
 *     var expanded by remember { mutableStateOf(false) }
 *     StylishSpeedDial(
 *         expanded = expanded,
 *         onExpandedChange = { expanded = it },
 *         actionCount = 2,
 *         modifier = Modifier.align(Alignment.BottomEnd),
 *         onActionClick = { index -> /* handle action */ },
 *         actions = { index ->
 *             StylishFab(
 *                 imageVector = actionsIcons[index],
 *                 contentDescription = "アクション $index",
 *                 onClick = { onActionClick(index) },
 *                 size = 40.dp,
 *             )
 *         },
 *     )
 * }
 * ```
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_speeddial` for UI
 * tests. Callers can override it by passing their own
 * `Modifier.testTag(...)` in [modifier].
 *
 * @param expanded Whether the actions are currently visible.
 * @param onExpandedChange Called with the new expanded state when the
 *   main button is tapped.
 * @param modifier Modifier applied to the root container (Column for
 *   [SpeedDialDirection.Up]/[SpeedDialDirection.Down], Row otherwise).
 *   Anchor this in a [Box] with `align(Alignment.BottomEnd)` for the
 *   standard floating pattern.
 * @param fabIcon Content of the main button. Defaults to a plus [Icon]
 *   without label — provide labeled content (or an icon that rotates
 *   while expanded) as needed.
 * @param direction The direction the actions fan out in (see
 *   [SpeedDialDirection]).
 * @param actionCount The number of actions to compose. [actions] is
 *   invoked once per index `0 until actionCount`.
 * @param spacing The gap between the main button and the actions, and
 *   between adjacent actions. Defaults to
 *   [StylishTheme.dimensions.itemSpacing].
 * @param fabSize The diameter of the main button. Defaults to
 *   [DefaultStylishDimensions.fabSize].
 * @param fabContainerColor Background color override for the main
 *   button. When `null`, [StylishFab]'s default applies.
 * @param fabContentColor Content color override for the main button.
 *   When `null`, [StylishFab]'s default applies.
 * @param onActionClick Convenience callback for action taps; invoke it
 *   from within [actions] with the item's index.
 * @param actions Slot composing one action per index. Render a small
 *   button here (see the usage pattern above).
 *
 * @see StylishFab
 * @see SpeedDialDirection
 * @see SpeedDialScope
 */
@Composable
public fun StylishSpeedDial(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    fabIcon: @Composable () -> Unit = { Icon(Icons.Default.Add, contentDescription = null) },
    direction: SpeedDialDirection = SpeedDialDirection.Up,
    actionCount: Int = 0,
    spacing: Dp = StylishTheme.dimensions.itemSpacing,
    fabSize: Dp = DefaultStylishDimensions.fabSize,
    fabContainerColor: Color? = null,
    fabContentColor: Color? = null,
    onActionClick: (Int) -> Unit = {},
    actions: @Composable SpeedDialScope.(index: Int) -> Unit,
) {
    val reducedMotion = isStylishReducedMotionEnabled()
    val containerModifier = modifier
        .testTag("stylish_speeddial")
        .then(
            if (reducedMotion) {
                Modifier
            } else {
                Modifier.animateContentSize()
            },
        )
    val actionSlot: @Composable () -> Unit = {
        if (reducedMotion) {
            if (expanded) {
                StylishSpeedDialActions(
                    actionCount = actionCount,
                    expanded = expanded,
                    actions = actions,
                )
            }
        } else {
            val fadeSpec = tween<Float>(
                durationMillis = StylishTheme.animation.durationShort,
                easing = StylishTheme.animation.defaultEasing,
            )
            val enterTransition = fadeIn(fadeSpec) + when (direction) {
                SpeedDialDirection.Up -> expandVertically(
                    expandFrom = Alignment.Bottom,
                    clip = true,
                )
                SpeedDialDirection.Down -> expandVertically(
                    expandFrom = Alignment.Top,
                    clip = true,
                )
                SpeedDialDirection.Start -> expandHorizontally(
                    expandFrom = Alignment.End,
                    clip = true,
                )
                SpeedDialDirection.End -> expandHorizontally(
                    expandFrom = Alignment.Start,
                    clip = true,
                )
            }
            val exitTransition = fadeOut(fadeSpec) + when (direction) {
                SpeedDialDirection.Up -> shrinkVertically(
                    shrinkTowards = Alignment.Bottom,
                    clip = true,
                )
                SpeedDialDirection.Down -> shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    clip = true,
                )
                SpeedDialDirection.Start -> shrinkHorizontally(
                    shrinkTowards = Alignment.End,
                    clip = true,
                )
                SpeedDialDirection.End -> shrinkHorizontally(
                    shrinkTowards = Alignment.Start,
                    clip = true,
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = enterTransition,
                exit = exitTransition,
            ) {
                StylishSpeedDialActions(
                    actionCount = actionCount,
                    expanded = expanded,
                    actions = actions,
                )
            }
        }
    }
    val mainFab: @Composable () -> Unit = {
        StylishFab(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            onClick = { onExpandedChange(!expanded) },
            containerColor = fabContainerColor,
            contentColor = fabContentColor,
            size = fabSize,
            iconContent = fabIcon,
        )
    }
    when (direction) {
        SpeedDialDirection.Up -> Column(
            modifier = containerModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            actionSlot()
            mainFab()
        }
        SpeedDialDirection.Down -> Column(
            modifier = containerModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            mainFab()
            actionSlot()
        }
        SpeedDialDirection.Start -> Row(
            modifier = containerModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing),
        ) {
            actionSlot()
            mainFab()
        }
        SpeedDialDirection.End -> Row(
            modifier = containerModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing),
        ) {
            mainFab()
            actionSlot()
        }
    }
}

@Composable
private fun StylishSpeedDialActions(
    actionCount: Int,
    expanded: Boolean,
    actions: @Composable SpeedDialScope.(index: Int) -> Unit,
) {
    repeat(actionCount) { index ->
        StylishSpeedDialScope(index = index, expanded = expanded).let { scope ->
            scope.actions(index)
        }
    }
}

@Preview(name = "Stylish speed dial up", showBackground = true, widthDp = 393)
@Composable
private fun StylishSpeedDialUpPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            var expanded by remember { mutableStateOf(false) }
            StylishSpeedDial(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                actionCount = 3,
                actions = { index ->
                    val icon = when (index) {
                        0 -> Icons.Default.Edit
                        1 -> Icons.Default.Share
                        else -> Icons.Default.Delete
                    }
                    Surface(
                        onClick = {},
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shadowElevation = 2.dp,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                },
            )
        }
    }
}

@Preview(name = "Stylish speed dial start", showBackground = true, widthDp = 393)
@Composable
private fun StylishSpeedDialStartPreview() {
    StylishTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            var expanded by remember { mutableStateOf(true) }
            StylishSpeedDial(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                direction = SpeedDialDirection.Start,
                actionCount = 2,
                actions = { index ->
                    val icon = if (index == 0) Icons.Default.Edit else Icons.Default.Share
                    Surface(
                        onClick = {},
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shadowElevation = 2.dp,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                },
            )
        }
    }
}
