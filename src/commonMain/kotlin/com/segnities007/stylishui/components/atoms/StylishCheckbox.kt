package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A checkbox for marking an independent option as selected or not,
 * wrapping the Material 3 [Checkbox] so it can be themed consistently
 * with the rest of Stylish UI. Use a checkbox when the selection is
 * confirmed elsewhere (e.g. on form submit); for immediate on/off
 * behaviour prefer [StylishSwitch], and for mutually exclusive choices
 * prefer [StylishRadioButton].
 *
 * @param checked Whether the checkbox is currently checked.
 * @param onCheckedChange Called with the new state when the user
 *   toggles the checkbox. `null` makes the checkbox display-only.
 * @param modifier Modifier applied to the [Checkbox] root. The root
 *   carries the default test tag `stylish_checkbox` for UI tests;
 *   callers can override it by passing their own `Modifier.testTag(...)`
 *   here.
 * @param enabled When `false`, the checkbox ignores pointer input and
 *   renders in the disabled color scheme.
 * @param colors Colors used in each state. Defaults to
 *   [CheckboxDefaults.colors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   checkbox, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 *
 * @see StylishSwitch
 * @see StylishRadioButton
 */
@Composable
public fun StylishCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.testTag("stylish_checkbox"),
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(name = "Stylish checkbox states", showBackground = true, widthDp = 393)
@Composable
private fun StylishCheckboxPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishCheckbox(checked = true, onCheckedChange = {})
                StylishCheckbox(checked = false, onCheckedChange = {})
                StylishCheckbox(checked = true, onCheckedChange = null, enabled = false)
                StylishCheckbox(checked = false, onCheckedChange = null, enabled = false)
            }
        }
    }
}
