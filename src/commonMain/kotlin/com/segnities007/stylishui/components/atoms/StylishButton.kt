package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isActionable
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors
import com.segnities007.stylishui.tokens.DefaultStylishDimensions

/**
 * The visual style of a [StylishButton].
 *
 * Mirrors the Material 3 button family: [Filled] is the high-emphasis default,
 * [Tonal] adds a tonal container, [Outlined] adds a hairline border on a
 * transparent container, [Text] is a bare label with no container, and
 * [Elevated] lifts the button with a floating shadow.
 *
 * @see StylishButton
 */
public enum class StylishButtonVariant {
    /** High-emphasis button: grouped-container fill, hairline border, interactive elevation. */
    Filled,

    /** Medium-emphasis button: secondary-container fill, no border, no elevation. */
    Tonal,

    /** Medium-emphasis button: transparent container with a hairline border. */
    Outlined,

    /** Low-emphasis button: transparent container, no border, no elevation. */
    Text,

    /** Medium-emphasis button: grouped-container fill with a floating shadow. */
    Elevated,
}

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
 * When [isLoading] is `true` and the button is [enabled], the label
 * row is replaced by a small spinner and clicks are ignored until
 * loading completes.
 *
 * @param onClick Called when the button is tapped.
 * @param modifier Modifier applied to the [Button] root.
 * @param variant The visual style of the button, controlling the
 *   default [colors], [border], and [elevation] (see
 *   [StylishButtonVariant]).
 * @param enabled When `false`, the button ignores pointer input and
 *   renders at zero elevation.
 * @param colors [ButtonColors] for the button. When `null` (default),
 *   resolved from [variant]: grouped-container colors for [StylishButtonVariant.Filled]
 *   and [StylishButtonVariant.Elevated], secondary-container colors for
 *   [StylishButtonVariant.Tonal], a transparent container with on-surface
 *   content for [StylishButtonVariant.Outlined], and a transparent container
 *   with primary content for [StylishButtonVariant.Text].
 * @param shape Corner shape. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param border Border stroke drawn around the button. When `null`
 *   (default), resolved from [variant]: a hairline of
 *   [StylishTheme.dimensions.outlineWidth] using
 *   `MaterialTheme.colorScheme.outlineVariant` for
 *   [StylishButtonVariant.Filled] and [StylishButtonVariant.Outlined],
 *   and no border for the other variants. Pass an explicit `BorderStroke`
 *   to override, or pass a non-null transparent stroke to remove it.
 * @param elevation Shadow elevation for the button. When `null`
 *   (default), resolved from [variant]: interactive elevation for
 *   [StylishButtonVariant.Filled], floating elevation
 *   ([StylishTheme.dimensions.floatingElevation]) pressed to 0 dp for
 *   [StylishButtonVariant.Elevated], and zero for the other variants.
 * @param interactionSource The [MutableInteractionSource] for the
 *   button, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 * @param contentPadding Inner padding of the button. Defaults to the
 *   control padding tokens
 *   ([DefaultStylishDimensions.controlPadding] /
 *   [DefaultStylishDimensions.controlVerticalPadding]).
 * @param minHeight Minimum height of the button. Defaults to
 *   [DefaultStylishDimensions.buttonMinHeight].
 * @param contentArrangement Horizontal arrangement of the label row
 *   between the leading and trailing slots. Defaults to
 *   [Arrangement.Center].
 * @param isLoading When `true` and [enabled], the label row is
 *   replaced by a 20 dp spinner and clicks are blocked (no-op) until
 *   loading finishes. When the button is disabled, the spinner is not
 *   shown.
 * @param leadingContent Optional content before the label (e.g. an
 *   icon). Rendered in a fixed-alignment slot.
 * @param trailingContent Optional content after the label (e.g. a
 *   badge). Rendered in a fixed-alignment slot.
 * @param content The button label, typically a [Text] composable.
 *
 * @see StylishConnectedButtonRow
 * @see StylishConnectedButtonColumn
 * @see StylishConnectedButtonGrid
 * @see StylishButtonVariant
 */
@Composable
public fun StylishButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: StylishButtonVariant = StylishButtonVariant.Filled,
    enabled: Boolean = true,
    colors: ButtonColors? = null,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    border: BorderStroke? = null,
    elevation: ButtonElevation? = null,
    interactionSource: MutableInteractionSource? = null,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = DefaultStylishDimensions.controlPadding,
        vertical = DefaultStylishDimensions.controlVerticalPadding,
    ),
    minHeight: Dp = DefaultStylishDimensions.buttonMinHeight,
    contentArrangement: Arrangement.Horizontal = Arrangement.Center,
    isLoading: Boolean = false,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val resolvedEnabled = enabled && !isLoading
    val actionable = isActionable(enabled = resolvedEnabled, hasClickAction = true)
    val resolvedColors = colors ?: when (variant) {
        StylishButtonVariant.Filled -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
        StylishButtonVariant.Tonal -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        StylishButtonVariant.Outlined -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
        StylishButtonVariant.Text -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        )
        StylishButtonVariant.Elevated -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    }
    val resolvedBorder = border ?: when (variant) {
        StylishButtonVariant.Filled, StylishButtonVariant.Outlined -> BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        )
        else -> null
    }
    val resolvedElevation = elevation ?: when (variant) {
        StylishButtonVariant.Filled -> ButtonDefaults.buttonElevation(
            defaultElevation = StylishTheme.dimensions.interactiveElevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        )
        StylishButtonVariant.Tonal,
        StylishButtonVariant.Outlined,
        StylishButtonVariant.Text,
        -> ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        )
        StylishButtonVariant.Elevated -> ButtonDefaults.buttonElevation(
            defaultElevation = StylishTheme.dimensions.floatingElevation,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
        )
    }
    Button(
        onClick = onClick,
        enabled = actionable,
        modifier = modifier.heightIn(min = minHeight),
        shape = shape,
        colors = resolvedColors,
        elevation = resolvedElevation,
        border = resolvedBorder,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        if (isLoading && enabled) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = resolvedColors.contentColor,
                strokeWidth = 2.5.dp,
            )
        } else {
            if (leadingContent != null) {
                Row(content = leadingContent)
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = contentArrangement,
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
            if (trailingContent != null) {
                Row(content = trailingContent)
            }
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

@Preview(name = "Stylish button loading", showBackground = true, widthDp = 393)
@Composable
private fun StylishButtonLoadingPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishButton(onClick = {}, isLoading = true) { Text("保存する") }
        }
    }
}

@Preview(name = "Stylish button variants", showBackground = true, widthDp = 393)
@Composable
private fun StylishButtonVariantsPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StylishButton(onClick = {}) { Text("Filled") }
                StylishButton(onClick = {}, variant = StylishButtonVariant.Tonal) { Text("Tonal") }
                StylishButton(onClick = {}, variant = StylishButtonVariant.Outlined) { Text("Outlined") }
                StylishButton(onClick = {}, variant = StylishButtonVariant.Text) { Text("Text") }
                StylishButton(onClick = {}, variant = StylishButtonVariant.Elevated) { Text("Elevated") }
            }
        }
    }
}
