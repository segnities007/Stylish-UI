package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A keyboard-key indicator (kbd) showing a single shortcut label, in the
 * style of shadcn/ui and Chakra UI's `Kbd` component.
 *
 * Renders [text] centered inside a keycap-like container: a filled,
 * outlined rounded box with the label offset 1 dp downward, mimicking a
 * physical key whose face sits slightly above its bottom edge. Use it to
 * document keyboard shortcuts next to their actions, e.g.
 * `StylishKbd("Ctrl")` + `StylishKbd("K")`.
 *
 * The component is display-only and carries no interaction semantics.
 *
 * ## Testing
 *
 * The root carries the default test tag `stylish_kbd` for UI tests.
 * Callers can override it by passing their own `Modifier.testTag(...)`
 * in [modifier].
 *
 * @param text The key label (e.g. `"Ctrl"`, `"⇧"`, `"F2"`).
 * @param modifier Modifier applied to the root [Box].
 * @param containerColor Fill color of the keycap. Defaults to
 *   `MaterialTheme.colorScheme.surfaceContainerHigh`.
 * @param contentColor Color of [text]. Defaults to
 *   `MaterialTheme.colorScheme.onSurfaceVariant`.
 * @param textStyle Typography for [text]. Defaults to
 *   `MaterialTheme.typography.labelMedium`.
 * @param shape Corner shape of the keycap. Defaults to a 6 dp
 *   [RoundedCornerShape].
 * @param contentPadding Inner padding around [text]. Defaults to
 *   6 dp horizontal, 2 dp vertical.
 */
@Composable
public fun StylishKbd(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    shape: Shape = RoundedCornerShape(6.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
) {
    Box(
        modifier = modifier.testTag("stylish_kbd"),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(containerColor)
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = shape,
                )
                .padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = textStyle,
                color = contentColor,
                modifier = Modifier.offset(y = 1.dp),
            )
        }
    }
}

@Preview(name = "Stylish kbd", showBackground = true, widthDp = 393)
@Composable
private fun StylishKbdPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("保存:", style = MaterialTheme.typography.bodyMedium)
                StylishKbd("Ctrl")
                StylishKbd("S")
            }
        }
    }
}
