package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.components.models.StylishContentState
import com.segnities007.stylishui.components.models.StylishScreenDocument
import com.segnities007.stylishui.components.models.StylishScreenElement
import com.segnities007.stylishui.components.models.StylishScreenEvent
import com.segnities007.stylishui.components.models.StylishTextRole
import com.segnities007.stylishui.components.molecules.StylishContentStateHost
import com.segnities007.stylishui.components.organisms.StylishScreenElementContent
import com.segnities007.stylishui.structure.ContentColumn
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Constrained screen renderer for lists, details, settings and controlled editing.
 *
 * The host supplies a title, validated document/state and events, never rendering lambdas.
 * Header clearance, system bars, keyboard viewport, stable keys, text hierarchy and control
 * semantics are supplied by Stylish UI. An empty Content document uses the standard empty state.
 * [retryLabel] is visible text supplied by the host for localization.
 */
@Composable
public fun StylishScreen(
    title: String,
    state: StylishContentState<StylishScreenDocument>,
    onEvent: (StylishScreenEvent) -> Unit,
    retryLabel: String? = null,
) {
    require(title.isNotBlank()) { "Screen title must not be blank" }
    require(retryLabel == null || retryLabel.isNotBlank()) { "Retry label must not be blank" }
    val spacing = StylishTheme.dimensions.itemSpacing
    val resolved = if (state is StylishContentState.Content && state.value.elements.isEmpty()) {
        StylishContentState.Empty()
    } else state
    StylishModernScreen(
        modifier = Modifier.imePadding(),
        header = { StylishHeader(title = { StylishText(title, role = StylishTextRole.Title) }) },
        hideOnScroll = false,
    ) {
        if (resolved is StylishContentState.Content) {
            items(resolved.value.elements, key = { it.id }, contentType = { it::class }) { element ->
                ContentColumn(spacing, Modifier.padding(bottom = spacing)) {
                    StylishScreenElementContent(element, onEvent)
                }
            }
        } else {
            item {
                ScreenStatus(resolved, retryLabel, onEvent)
            }
        }
    }
}

@Composable
private fun ScreenStatus(
    state: StylishContentState<StylishScreenDocument>,
    retryLabel: String?,
    onEvent: (StylishScreenEvent) -> Unit,
) {
    ContentColumn(StylishTheme.dimensions.itemSpacing) {
        StylishContentStateHost(state) { }
        val label = retryLabel ?: (state as? StylishContentState.Error)?.retryLabel
        if (state is StylishContentState.Error && !label.isNullOrBlank()) {
            StylishButton(onClick = { onEvent(StylishScreenEvent.Retry) }) {
                StylishText(label)
            }
        }
    }
}

@Preview(name = "Constrained screen — light", widthDp = 393, heightDp = 600)
@Preview(name = "Constrained screen — dark", widthDp = 393, heightDp = 600, uiMode = 0x20)
@Composable
private fun StylishScreenPreview() {
    StylishTheme {
        StylishScreen(
            title = "Settings",
            state = StylishContentState.Content(StylishScreenDocument(listOf(
                StylishScreenElement.Input("name", "Name", "Alex"),
                StylishScreenElement.Toggle("sync", "Sync", true),
                StylishScreenElement.Action("save", "Save"),
            ))),
            onEvent = {},
        )
    }
}
