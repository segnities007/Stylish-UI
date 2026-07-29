package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/**
 * A standalone Stylish button with the same visual language as the
 * connected-button family — grouped-container colors, interactive
 * elevation, a hairline outline, and a 52 dp minimum height — but
 * without any connected-group geometry. Use this when a single button
 * is needed outside a [StylishConnectedButtonRow],
 * [StylishConnectedButtonColumn], or [StylishConnectedButtonGrid].
 *
 * The [leadingContent] and [trailingContent] slots are laid out in a
 * three-part row (leading | content | trailing) so icons align
 * consistently with the connected-button family.
 *
 * @param onClick Called when the button is tapped.
 * @param modifier Modifier applied to the [Button] root.
 * @param enabled When `false`, the button ignores pointer input and
 *   renders at zero elevation.
 * @param colors [ButtonColors] for the button. Defaults to
 *   grouped-container colors matching the connected-button family.
 * @param shape Corner shape. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param contentPadding Inner padding of the button.
 * @param minHeight Minimum height of the button. Defaults to 52.dp.
 * @param leadingContent Optional content before the label (e.g. an
 *   icon). Rendered in a fixed-alignment slot.
 * @param trailingContent Optional content after the label (e.g. a
 *   badge). Rendered in a fixed-alignment slot.
 * @param content The button label, typically a [Text] composable.
 *
 * @see StylishConnectedButtonRow
 * @see StylishConnectedButtonColumn
 * @see StylishConnectedButtonGrid
 */
@Composable
public fun StylishButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    minHeight: Dp = 52.dp,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val actionable = isActionable(enabled = enabled, hasClickAction = true)
    Button(
        onClick = onClick,
        enabled = actionable,
        modifier = modifier.heightIn(min = minHeight),
        shape = shape,
        colors = colors,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = StylishTheme.dimensions.interactiveElevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        border = BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        ),
        contentPadding = contentPadding,
    ) {
        if (leadingContent != null) {
            Row(content = leadingContent)
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
        if (trailingContent != null) {
            Row(content = trailingContent)
        }
    }
}

@Preview(name = "Stylish button", showBackground = true, widthDp = 393)
@Composable
private fun StylishButtonPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishButton(onClick = {}) { Text("保存する") }
        }
    }
}

@Preview(name = "Stylish button disabled", showBackground = true, widthDp = 393)
@Composable
private fun StylishButtonDisabledPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishButton(onClick = {}, enabled = false) { Text("保存する") }
        }
    }
}