package com.segnities007.stylishui.components.molecules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.theme.StylishTheme

/** The axis on which [StylishButtonGroup] lays out its children. */
public enum class StylishButtonGroupOrientation {
    /** Buttons share a horizontal toolbar-like row. */
    Horizontal,

    /** Buttons form a vertical action list. */
    Vertical,
}

/**
 * A flexible, connected container for related actions.
 *
 * Unlike a segmented control, a button group does not impose selection
 * semantics: each child can be an independent [StylishButton], an icon
 * button, or a custom slot. This makes it suitable for edit toolbars,
 * pagination actions, and destructive/secondary action pairs on both
 * desktop and mobile layouts. The group owns the clipping, border, and
 * background while callers retain full control of each child.
 *
 * @param modifier Modifier applied to the group surface.
 * @param orientation Direction in which children are arranged.
 * @param shape Shape used for the outer container and clipping.
 * @param containerColor Background color behind the children.
 * @param border Border drawn around the group. Pass `null` to omit it.
 * @param contentPadding Padding between the outer border and children.
 * @param spacing Space between children. Defaults to zero for a connected
 *   appearance; use [StylishTheme.dimensions.inlineSpacing] for separated
 *   actions.
 * @param content Child actions. The slot is independent of orientation so the
 *   same content can be reused in responsive layouts.
 */
@Composable
public fun StylishButtonGroup(
    modifier: Modifier = Modifier,
    orientation: StylishButtonGroupOrientation = StylishButtonGroupOrientation.Horizontal,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    spacing: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier),
        shape = shape,
        color = containerColor,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing),
        ) {
            if (orientation == StylishButtonGroupOrientation.Horizontal) {
                content()
            } else {
                // A ColumnScope adapter keeps the public slot stable while
                // allowing the same group to switch orientation at runtime.
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing),
                ) {
                    content()
                }
            }
        }
    }
}

@Preview(name = "Stylish button group", showBackground = true, widthDp = 393)
@Composable
private fun StylishButtonGroupPreview() {
    StylishTheme(darkTheme = false) {
        StylishButtonGroup(modifier = Modifier.padding(20.dp)) {
            StylishButton(onClick = {}) { androidx.compose.material3.Text("戻る") }
            StylishButton(onClick = {}) { androidx.compose.material3.Text("保存") }
        }
    }
}
