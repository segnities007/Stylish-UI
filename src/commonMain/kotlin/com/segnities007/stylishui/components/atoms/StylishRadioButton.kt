package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A radio button for selecting exactly one option from a group,
 * wrapping the Material 3 [RadioButton] so it can be themed
 * consistently with the rest of Stylish UI. Use radio buttons for
 * mutually exclusive choices; for immediate on/off behaviour prefer
 * [StylishSwitch], and for independent yes/no marks prefer
 * [StylishCheckbox].
 *
 * @param selected Whether the radio button is currently selected.
 * @param onClick Called when the user taps the radio button. `null`
 *   makes the radio button display-only.
 * @param modifier Modifier applied to the [RadioButton] root. The root
 *   carries the default test tag `stylish_radiobutton` for UI tests;
 *   callers can override it by passing their own `Modifier.testTag(...)`
 *   here.
 * @param enabled When `false`, the radio button ignores pointer input
 *   and renders in the disabled color scheme.
 * @param colors Colors used in each state. Defaults to
 *   [RadioButtonDefaults.colors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   radio button, used to observe press/focus/hover interactions.
 *   When `null`, an internal one is remembered.
 *
 * @see StylishSwitch
 * @see StylishCheckbox
 */
@Composable
public fun StylishRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier.testTag("stylish_radiobutton"),
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(name = "Stylish radio button states", showBackground = true, widthDp = 393)
@Composable
private fun StylishRadioButtonPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishRadioButton(selected = true, onClick = {})
                StylishRadioButton(selected = false, onClick = {})
                StylishRadioButton(selected = true, onClick = null, enabled = false)
                StylishRadioButton(selected = false, onClick = null, enabled = false)
            }
        }
    }
}
