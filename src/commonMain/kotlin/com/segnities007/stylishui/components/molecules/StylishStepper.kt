package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A horizontal step indicator (the web "Stepper" pattern from MUI and
 * Chakra UI).
 *
 * Each step is drawn as a numbered (or check-marked) circle connected by
 * a line to the next step. Completed steps show a check mark, the current
 * step is highlighted, and later steps are muted. Steps can be made
 * tappable via [onStepClick] for free navigation through a flow.
 *
 * @param steps The step labels, in order.
 * @param modifier Modifier applied to the root row.
 * @param currentStep Index of the active step. Defaults to `0`.
 * @param completedSteps Indices of steps that are already finished.
 *   Defaults to an empty set.
 * @param enabled When `false`, step circles are not clickable.
 * @param onStepClick Optional callback invoked when a step circle is
 *   tapped. When `null`, steps are display-only.
 * @param circleSize Diameter of each step circle. Defaults to 28 dp.
 * @param connectorHeight Thickness of the line between steps. Defaults
 *   to 2 dp.
 * @param labelStyle Typography of the step labels. Defaults to
 *   [MaterialTheme.typography.labelMedium].
 * @param completedColor Fill of completed step circles. Defaults to
 *   [MaterialTheme.colorScheme.primary].
 * @param activeColor Fill of the current step circle. Defaults to
 *   [MaterialTheme.colorScheme.primary].
 * @param inactiveColor Fill of future step circles. Defaults to
 *   [MaterialTheme.colorScheme.surfaceVariant].
 * @param contentColor Icon/number color inside the circles. Defaults to
 *   [MaterialTheme.colorScheme.onSurface].
 */
@Composable
public fun StylishStepper(
    steps: List<String>,
    modifier: Modifier = Modifier,
    currentStep: Int = 0,
    completedSteps: Set<Int> = emptySet(),
    enabled: Boolean = true,
    onStepClick: ((Int) -> Unit)? = null,
    circleSize: Dp = 28.dp,
    connectorHeight: Dp = 2.dp,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    completedColor: Color = MaterialTheme.colorScheme.primary,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = modifier.stylishTestTag("stepper").fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        steps.forEachIndexed { index, label ->
            val isCompleted = index in completedSteps
            val isActive = index == currentStep
            val circleColor = when {
                isCompleted -> completedColor
                isActive -> activeColor
                else -> inactiveColor
            }
            val stepClickable = enabled && onStepClick != null

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (index > 0) {
                        val previousCompleted = (index - 1) in completedSteps
                        Box(
                            Modifier
                                .weight(1f)
                                .height(connectorHeight)
                                .background(
                                    if (previousCompleted) completedColor else inactiveColor,
                                ),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(circleSize)
                            .semantics(mergeDescendants = true) {
                                selected = isActive
                                if (stepClickable) role = Role.Button
                            }
                            .then(
                                if (stepClickable) {
                                    Modifier.clickable { onStepClick(index) }
                                } else {
                                    Modifier
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            Modifier
                                .size(circleSize)
                                .background(circleColor, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (isCompleted) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = contentColor,
                                    modifier = Modifier.size(16.dp),
                                )
                            } else {
                                Text(
                                    (index + 1).toString(),
                                    style = labelStyle,
                                    color = if (isActive) contentColor else onSurface.copy(alpha = 0.6f),
                                )
                            }
                        }
                    }
                    if (index < steps.lastIndex) {
                        val thisCompleted = index in completedSteps
                        Box(
                            Modifier
                                .weight(1f)
                                .height(connectorHeight)
                                .background(
                                    if (thisCompleted) completedColor else inactiveColor,
                                ),
                        )
                    }
                }
                Text(
                    label,
                    style = labelStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(top = StylishTheme.dimensions.inlineSpacing),
                )
            }
        }
    }
}

@Preview(name = "Stylish stepper", showBackground = true, widthDp = 393)
@Composable
private fun StylishStepperPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishStepper(
                steps = listOf("車両情報", "点検項目", "確認"),
                currentStep = 1,
                completedSteps = setOf(0),
            )
        }
    }
}
