package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishOutlinedTextField
import com.segnities007.stylishui.components.atoms.StylishSwitch
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.components.models.StylishScreenElement
import com.segnities007.stylishui.components.models.StylishScreenEvent
import com.segnities007.stylishui.components.models.StylishTextRole
import com.segnities007.stylishui.components.molecules.StylishListItem
import com.segnities007.stylishui.structure.ContentRow
import com.segnities007.stylishui.theme.StylishTheme

@Composable
internal fun StylishScreenElementContent(
    element: StylishScreenElement,
    onEvent: (StylishScreenEvent) -> Unit,
) {
    val modifier = Modifier.fillMaxWidth().testTag("screen_element:${element.id}")
    when (element) {
        is StylishScreenElement.Text -> StylishText(element.text, modifier, element.role)
        is StylishScreenElement.Entry -> ScreenEntry(element, modifier, onEvent)
        is StylishScreenElement.Input -> ScreenInput(element, modifier, onEvent)
        is StylishScreenElement.Toggle -> ScreenToggle(element, modifier, onEvent)
        is StylishScreenElement.Action -> StylishButton(
            onClick = { onEvent(StylishScreenEvent.Activate(element.id)) },
            modifier = modifier.semantics { contentDescription = element.label },
            enabled = element.enabled,
            isLoading = element.loading,
        ) { StylishText(element.label, role = StylishTextRole.Label) }
    }
}

@Composable
private fun ScreenEntry(
    entry: StylishScreenElement.Entry,
    modifier: Modifier,
    onEvent: (StylishScreenEvent) -> Unit,
) {
    StylishListItem(
        title = entry.title,
        supportingText = entry.supportingText,
        modifier = modifier,
        enabled = entry.enabled,
        onClick = entry.actionId?.let { action ->
            { onEvent(StylishScreenEvent.Activate(action)) }
        },
    )
}

@Composable
private fun ScreenInput(
    input: StylishScreenElement.Input,
    modifier: Modifier,
    onEvent: (StylishScreenEvent) -> Unit,
) {
    StylishOutlinedTextField(
        value = input.value,
        onValueChange = { onEvent(StylishScreenEvent.Edit(input.id, it)) },
        modifier = modifier,
        label = { StylishText(input.label) },
        supportingText = input.error?.let { message -> { StylishText(message) } },
        isError = input.error != null,
        enabled = input.enabled,
    )
}

@Composable
private fun ScreenToggle(
    toggle: StylishScreenElement.Toggle,
    modifier: Modifier,
    onEvent: (StylishScreenEvent) -> Unit,
) {
    ContentRow(
        StylishTheme.dimensions.itemSpacing,
        modifier.toggleable(
            value = toggle.checked,
            enabled = toggle.enabled,
            role = Role.Switch,
            onValueChange = { onEvent(StylishScreenEvent.Toggle(toggle.id, it)) },
        ).heightIn(min = StylishTheme.dimensions.iconButtonMinSize)
            .padding(StylishTheme.dimensions.controlPadding),
    ) {
        StylishText(
            toggle.label,
            Modifier.weight(1f),
            color = StylishTheme.colorScheme.onSurface.copy(alpha = if (toggle.enabled) 1f else 0.38f),
        )
        StylishSwitch(toggle.checked, onCheckedChange = null, enabled = toggle.enabled)
    }
}
