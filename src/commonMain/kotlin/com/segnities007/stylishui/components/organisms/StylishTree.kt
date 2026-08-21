package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.stylishui.foundation.stylishInteractiveTarget
import com.segnities007.stylishui.foundation.headless.StylishReducer
import com.segnities007.stylishui.theme.StylishTheme

/** A recursive, keyboard-actionable tree node. */
@Immutable
public data class StylishTreeNode<T>(
    public val id: Any,
    public val label: String,
    public val value: T,
    public val children: List<StylishTreeNode<T>> = emptyList(),
)

/** Hoisted interaction state for [StylishTree]. */
@Immutable
public data class StylishTreeState(
    public val expandedIds: Set<Any> = emptySet(),
    public val selectedId: Any? = null,
    public val focusedId: Any? = null,
)

/** Pure actions accepted by [StylishTreeState.reduce]. */
@Immutable
public sealed interface StylishTreeAction {
    @Immutable
    public data class ToggleExpanded(public val id: Any) : StylishTreeAction

    @Immutable
    public data class SetExpanded(public val ids: Set<Any>) : StylishTreeAction

    @Immutable
    public data class Select(public val id: Any?) : StylishTreeAction

    @Immutable
    public data class Focus(public val id: Any?) : StylishTreeAction
}

/** Shared pure reducer used by Compose, SwiftUI, Web, and desktop host stores. */
public object StylishTreeStateReducer : StylishReducer<StylishTreeState, StylishTreeAction> {
    override fun reduce(state: StylishTreeState, action: StylishTreeAction): StylishTreeState = when (action) {
        is StylishTreeAction.ToggleExpanded -> state.copy(
            expandedIds = if (action.id in state.expandedIds) {
                state.expandedIds - action.id
            } else {
                state.expandedIds + action.id
            },
        )
        is StylishTreeAction.SetExpanded -> state.copy(expandedIds = action.ids)
        is StylishTreeAction.Select -> state.copy(selectedId = action.id, focusedId = action.id)
        is StylishTreeAction.Focus -> state.copy(focusedId = action.id)
    }
}

/** Returns the next tree state without coupling state transitions to Compose. */
public fun StylishTreeState.reduce(action: StylishTreeAction): StylishTreeState =
    StylishTreeStateReducer.reduce(this, action)

/** A controlled tree whose flattened visible rows are virtualized by [LazyColumn]. */
@Composable
@NonRestartableComposable
public fun <T> StylishTree(
    nodes: List<StylishTreeNode<T>>,
    modifier: Modifier = Modifier,
    expandedIds: Set<Any> = emptySet(),
    onExpandedIdsChange: ((Set<Any>) -> Unit)? = null,
    selectedId: Any? = null,
    onSelectedIdChange: ((Any) -> Unit)? = null,
) {
    StylishTreeContent(
        nodes = nodes,
        modifier = modifier,
        expandedIds = expandedIds,
        onExpandedIdsChange = onExpandedIdsChange,
        selectedId = selectedId,
        onSelectedIdChange = onSelectedIdChange,
    )
}

/**
 * Controlled tree overload using a single hoisted state object and pure actions.
 * Use this form when navigation state must be persisted, replayed, or shared with a
 * view-model/store. The legacy expanded/selected callbacks remain source-compatible.
 */
@Composable
@NonRestartableComposable
public fun <T> StylishTree(
    nodes: List<StylishTreeNode<T>>,
    state: StylishTreeState,
    onStateChange: (StylishTreeState) -> Unit,
    modifier: Modifier = Modifier,
) {
    StylishTreeContent(
        nodes = nodes,
        modifier = modifier,
        expandedIds = state.expandedIds,
        onExpandedIdsChange = { ids -> onStateChange(state.copy(expandedIds = ids)) },
        selectedId = state.selectedId,
        onSelectedIdChange = { id -> onStateChange(state.copy(selectedId = id, focusedId = id)) },
        focusedId = state.focusedId,
        onFocusedIdChange = { id -> onStateChange(state.copy(focusedId = id)) },
    )
}

@Composable
private fun <T> StylishTreeContent(
    nodes: List<StylishTreeNode<T>>,
    modifier: Modifier,
    expandedIds: Set<Any>,
    onExpandedIdsChange: ((Set<Any>) -> Unit)?,
    selectedId: Any?,
    onSelectedIdChange: ((Any) -> Unit)?,
    focusedId: Any? = null,
    onFocusedIdChange: ((Any) -> Unit)? = null,
) {
    val visibleNodes = androidx.compose.runtime.remember(nodes, expandedIds) {
        flattenStylishTree(nodes, expandedIds)
    }
    LazyColumn(modifier.testTag("stylish_tree")) {
        items(visibleNodes, key = { it.node.id }) { visible ->
            StylishTreeNodeRow(
                node = visible.node,
                depth = visible.depth,
                expandedIds = expandedIds,
                onExpandedIdsChange = onExpandedIdsChange,
                selectedId = selectedId,
                onSelectedIdChange = onSelectedIdChange,
                focusedId = focusedId,
                onFocusedIdChange = onFocusedIdChange,
            )
        }
    }
}

@Composable
private fun <T> StylishTreeNodeRow(
    node: StylishTreeNode<T>,
    depth: Int,
    expandedIds: Set<Any>,
    onExpandedIdsChange: ((Set<Any>) -> Unit)?,
    selectedId: Any?,
    onSelectedIdChange: ((Any) -> Unit)?,
    focusedId: Any?,
    onFocusedIdChange: ((Any) -> Unit)?,
) {
    val expanded = node.id in expandedIds
    val strings = StylishTheme.strings
    val focusRequester = androidx.compose.runtime.remember(node.id) { FocusRequester() }
    androidx.compose.runtime.LaunchedEffect(focusedId) {
        if (focusedId == node.id) focusRequester.requestFocus()
    }
    val toggleExpansion: () -> Unit = {
        if (node.children.isNotEmpty()) {
            onExpandedIdsChange?.invoke(if (expanded) expandedIds - node.id else expandedIds + node.id)
        }
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = (depth * 20).dp)
            .focusRequester(focusRequester)
            .focusable()
            .onFocusChanged { state -> if (state.isFocused) onFocusedIdChange?.invoke(node.id) }
            .stylishInteractiveTarget()
            .clickable { onSelectedIdChange?.invoke(node.id) }
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Enter, Key.Spacebar -> {
                        onSelectedIdChange?.invoke(node.id)
                        toggleExpansion()
                        true
                    }
                    Key.DirectionRight -> if (node.children.isNotEmpty() && !expanded) { toggleExpansion(); true } else false
                    Key.DirectionLeft -> if (node.children.isNotEmpty() && expanded) { toggleExpansion(); true } else false
                    else -> false
                }
            }
            .semantics {
                contentDescription = node.label
                role = Role.Button
                selected = node.id == selectedId
                if (node.children.isNotEmpty()) stateDescription = if (expanded) strings.collapseRow else strings.expandRow
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (node.children.isNotEmpty()) {
            IconButton(
                onClick = { toggleExpansion() },
                modifier = Modifier.stylishInteractiveTarget(),
            ) {
                Icon(
                    if (expanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                    contentDescription = if (expanded) strings.collapseRow else strings.expandRow,
                )
            }
        } else {
            Text("•", Modifier.widthIn(min = 48.dp).padding(horizontal = 16.dp))
        }
        Text(node.label, Modifier.padding(vertical = 10.dp))
    }
}

@Preview
@Composable
private fun StylishTreePreview() {
    StylishTree(
        nodes = listOf(
            StylishTreeNode("root", "Root", Unit, listOf(StylishTreeNode("child", "Child", Unit))),
        ),
        expandedIds = setOf("root"),
    )
}
