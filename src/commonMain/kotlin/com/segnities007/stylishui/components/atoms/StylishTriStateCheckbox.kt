package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A tri-state checkbox for expressing partial selection, wrapping the
 * Material 3 [TriStateCheckbox] so it can be themed consistently with
 * the rest of Stylish UI.
 *
 * Use this for the parent of a group of [StylishCheckbox]es: checked
 * when every child is checked, [ToggleableState.Indeterminate] when
 * some (but not all) children are checked, and unchecked when none
 * are. The child group drives the state; this checkbox only reflects
 * it (the caller typically passes a toggle that cycles the state).
 *
 * @param state Whether the checkbox is checked, unchecked, or in the
 *   [ToggleableState.Indeterminate] state.
 * @param onClick Called when the user taps the checkbox. `null` makes
 *   the checkbox display-only.
 * @param modifier Modifier applied to the [TriStateCheckbox] root.
 * @param enabled When `false`, the checkbox ignores pointer input and
 *   renders in the disabled color scheme.
 * @param colors Colors used in each state. Defaults to
 *   [CheckboxDefaults.colors].
 * @param interactionSource The [MutableInteractionSource] for the
 *   checkbox, used to observe press/focus/hover interactions. When
 *   `null`, an internal one is remembered.
 *
 * @see StylishCheckbox
 */
@Composable
public fun StylishTriStateCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    TriStateCheckbox(
        state = state,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview(name = "Stylish tri-state checkbox states", showBackground = true, widthDp = 393)
@Composable
private fun StylishTriStateCheckboxPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StylishTriStateCheckbox(state = ToggleableState.On, onClick = {})
                StylishTriStateCheckbox(state = ToggleableState.Indeterminate, onClick = {})
                StylishTriStateCheckbox(state = ToggleableState.Off, onClick = {})
                StylishTriStateCheckbox(state = ToggleableState.On, onClick = null, enabled = false)
            }
        }
    }
}
