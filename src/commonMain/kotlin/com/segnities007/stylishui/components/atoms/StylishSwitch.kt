package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A toggle switch for turning a single option on or off, wrapping the
 * Material 3 [Switch] so it can be themed consistently with the rest of
 * Stylish UI. Use a switch when the change takes effect immediately
 * (e.g. enabling a setting); for mutually exclusive choices prefer
 * [StylishRadioButton], and for independent yes/no marks prefer
 * [StylishCheckbox].
 *
 * @param checked Whether the switch is currently on.
 * @param onCheckedChange Called with the new state when the user
 *   toggles the switch. `null` makes the switch display-only.
 * @param modifier Modifier applied to the [Switch] root. The root
 *   carries the default test tag `stylish_switch` for UI tests;
 *   callers can override it by passing their own `Modifier.testTag(...)`
 *   here.
 * @param enabled When `false`, the switch ignores pointer input and
 *   renders in the disabled color scheme.
 * @param colors Colors used in each state. Defaults to
 *   [SwitchDefaults.colors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   switch, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 *
 * @see StylishCheckbox
 * @see StylishRadioButton
 */
@Composable
public fun StylishSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.testTag("stylish_switch"),
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(name = "Stylish switch states", showBackground = true, widthDp = 393)
@Composable
private fun StylishSwitchPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishSwitch(checked = true, onCheckedChange = {})
                StylishSwitch(checked = false, onCheckedChange = {})
                StylishSwitch(checked = true, onCheckedChange = null, enabled = false)
                StylishSwitch(checked = false, onCheckedChange = null, enabled = false)
            }
        }
    }
}
