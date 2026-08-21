package com.segnities007.stylishui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.rememberStylishFocusRequesters
import com.segnities007.stylishui.foundation.stylishRovingFocus
import com.segnities007.stylishui.foundation.stylishInteractiveTarget
import com.segnities007.stylishui.foundation.headless.StylishReducer
import com.segnities007.stylishui.theme.StylishTheme

/** An item rendered by [StylishTransfer]. */
@Immutable
public data class StylishTransferItem<T>(public val key: Any, public val value: T, public val label: String)

/** Selection behavior for highlighted rows in [StylishTransfer]. */
public enum class StylishTransferSelectionMode { Single, Multiple }

/** Hoisted selection/highlight state for [StylishTransfer]. */
@Immutable
public data class StylishTransferState(
    public val selectedKeys: Set<Any> = emptySet(),
    public val highlightedKeys: Set<Any> = emptySet(),
)

/** Pure actions accepted by [StylishTransferState.reduce]. */
@Immutable
public sealed interface StylishTransferAction {
    @Immutable
    public data class ToggleHighlighted(public val key: Any) : StylishTransferAction

    @Immutable
    public data class SetHighlighted(public val keys: Set<Any>) : StylishTransferAction

    @Immutable
    public data class SetSelected(public val keys: Set<Any>) : StylishTransferAction

    @Immutable
    public data object MoveHighlightedToSelected : StylishTransferAction

    @Immutable
    public data object MoveHighlightedToAvailable : StylishTransferAction
}

/** Shared pure reducer used by Compose, SwiftUI, Web, and desktop host stores. */
public object StylishTransferStateReducer : StylishReducer<StylishTransferState, StylishTransferAction> {
    override fun reduce(state: StylishTransferState, action: StylishTransferAction): StylishTransferState = when (action) {
        is StylishTransferAction.ToggleHighlighted -> state.copy(
            highlightedKeys = if (action.key in state.highlightedKeys) {
                state.highlightedKeys - action.key
            } else {
                state.highlightedKeys + action.key
            },
        )
        is StylishTransferAction.SetHighlighted -> state.copy(highlightedKeys = action.keys)
        is StylishTransferAction.SetSelected -> state.copy(selectedKeys = action.keys)
        StylishTransferAction.MoveHighlightedToSelected -> state.copy(
            selectedKeys = state.selectedKeys + state.highlightedKeys,
            highlightedKeys = emptySet(),
        )
        StylishTransferAction.MoveHighlightedToAvailable -> state.copy(
            selectedKeys = state.selectedKeys - state.highlightedKeys,
            highlightedKeys = emptySet(),
        )
    }
}

/** Returns the next transfer state without coupling selection to Compose. */
public fun StylishTransferState.reduce(action: StylishTransferAction): StylishTransferState =
    StylishTransferStateReducer.reduce(this, action)

/**
 * A controlled dual-list transfer control for moving items between panes.
 *
 * [selectionMode] defaults to [StylishTransferSelectionMode.Multiple], so users can build a
 * batch before moving it. Set [StylishTransferSelectionMode.Single] for one-at-a-time workflows.
 */
@Composable
@NonRestartableComposable
public fun <T> StylishTransfer(
    available: List<StylishTransferItem<T>>,
    selectedKeys: Set<Any>,
    onSelectedKeysChange: (Set<Any>) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    selectedTitle: String = "",
    selectionMode: StylishTransferSelectionMode = StylishTransferSelectionMode.Multiple,
) {
    var highlighted by remember { mutableStateOf<Set<Any>>(emptySet()) }
    StylishTransferContent(
        available = available,
        selectedKeys = selectedKeys,
        onSelectedKeysChange = onSelectedKeysChange,
        modifier = modifier,
        title = title,
        selectedTitle = selectedTitle,
        selectionMode = selectionMode,
        highlighted = highlighted,
        onHighlightedChange = { highlighted = it },
    )
}

/**
 * Controlled transfer overload using a single hoisted state object and pure actions.
 * This allows selection/highlight state to be persisted or shared with a store.
 */
@Composable
@NonRestartableComposable
public fun <T> StylishTransfer(
    available: List<StylishTransferItem<T>>,
    state: StylishTransferState,
    onStateChange: (StylishTransferState) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    selectedTitle: String = "",
    selectionMode: StylishTransferSelectionMode = StylishTransferSelectionMode.Multiple,
) {
    StylishTransferContent(
        available = available,
        selectedKeys = state.selectedKeys,
        onSelectedKeysChange = { keys -> onStateChange(state.copy(selectedKeys = keys)) },
        modifier = modifier,
        title = title,
        selectedTitle = selectedTitle,
        selectionMode = selectionMode,
        highlighted = state.highlightedKeys,
        onHighlightedChange = { keys -> onStateChange(state.copy(highlightedKeys = keys)) },
    )
}

@Composable
private fun <T> StylishTransferContent(
    available: List<StylishTransferItem<T>>,
    selectedKeys: Set<Any>,
    onSelectedKeysChange: (Set<Any>) -> Unit,
    modifier: Modifier,
    title: String,
    selectedTitle: String,
    selectionMode: StylishTransferSelectionMode,
    highlighted: Set<Any>,
    onHighlightedChange: (Set<Any>) -> Unit,
) {
    val strings = StylishTheme.strings
    val availableLabel = title.ifBlank { strings.available }
    val selectedLabel = selectedTitle.ifBlank { strings.selected }
    var focusedKey by remember { mutableStateOf<Any?>(null) }
    Row(modifier.testTag("stylish_transfer"), horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.itemSpacing)) {
        TransferPane(
            title = availableLabel,
            items = available.filter { it.key !in selectedKeys },
            highlighted = highlighted,
            onClick = { key -> onHighlightedChange(highlightedForTransferKey(highlighted, key, selectionMode)) },
            selectionStateLabel = { isHighlighted -> if (isHighlighted) strings.selected else strings.available },
            focusedKey = focusedKey,
            onFocusedKeyChange = { focusedKey = it },
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(
                onClick = {
                    val moved = highlighted.filter { it !in selectedKeys }.toSet()
                    onSelectedKeysChange(selectedKeys + moved)
                    onHighlightedChange(highlighted - moved)
                },
                enabled = highlighted.any { it !in selectedKeys },
                modifier = Modifier.semantics {
                    contentDescription = "Move to $selectedLabel"
                },
            ) { Text("→") }
            Button(
                onClick = {
                    val moved = highlighted.filter { it in selectedKeys }.toSet()
                    onSelectedKeysChange(selectedKeys - moved)
                    onHighlightedChange(highlighted - moved)
                },
                enabled = highlighted.any { it in selectedKeys },
                modifier = Modifier.semantics {
                    contentDescription = "Move to $availableLabel"
                },
            ) { Text("←") }
        }
        TransferPane(
            title = selectedLabel,
            items = available.filter { it.key in selectedKeys },
            highlighted = highlighted,
            onClick = { key -> onHighlightedChange(highlightedForTransferKey(highlighted, key, selectionMode)) },
            selectionStateLabel = { isHighlighted -> if (isHighlighted) strings.selected else strings.available },
            focusedKey = focusedKey,
            onFocusedKeyChange = { focusedKey = it },
        )
    }
}

private fun highlightedForTransferKey(
    current: Set<Any>,
    key: Any,
    mode: StylishTransferSelectionMode,
): Set<Any> = when (mode) {
    StylishTransferSelectionMode.Single -> setOf(key)
    StylishTransferSelectionMode.Multiple -> if (key in current) current - key else current + key
}

@Composable
private fun TransferPane(
    title: String,
    items: List<StylishTransferItem<*>>,
    highlighted: Set<Any>,
    onClick: (Any) -> Unit,
    selectionStateLabel: (Boolean) -> String,
    focusedKey: Any?,
    onFocusedKeyChange: (Any?) -> Unit,
) {
    val requesters = rememberStylishFocusRequesters(items.size)
    Surface(
        Modifier
            .widthIn(min = 140.dp)
            .fillMaxWidth(0.4f)
            .testTag("stylish_transfer_pane_$title")
            .semantics { collectionInfo = CollectionInfo(rowCount = items.size, columnCount = 1) },
        tonalElevation = 1.dp,
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.semantics { heading() })
            LazyColumn(Modifier.heightIn(max = 320.dp)) {
                itemsIndexed(items, key = { _, item -> item.key }) { index, item ->
                    val requester = requesters[index]
                    androidx.compose.runtime.LaunchedEffect(focusedKey, item.key) {
                        if (focusedKey == item.key) requester.requestFocus()
                    }
                    Text(
                        item.label,
                        Modifier
                            .fillMaxWidth()
                            .testTag("stylish_transfer_item_${item.key}")
                            .stylishRovingFocus(
                                requester = requester,
                                index = index,
                                itemCount = items.size,
                                onMove = { target ->
                                    requesters.getOrNull(target)?.requestFocus()
                                    onFocusedKeyChange(items.getOrNull(target)?.key)
                                },
                                onActivate = {
                                    onFocusedKeyChange(item.key)
                                    onClick(item.key)
                                },
                            )
                            .onFocusChanged { state ->
                                if (state.isFocused) onFocusedKeyChange(item.key)
                            }
                            .stylishInteractiveTarget()
                            .clickable {
                                onFocusedKeyChange(item.key)
                                onClick(item.key)
                            }
                            .background(if (item.key in highlighted) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
                            .padding(8.dp)
                            .semantics {
                                contentDescription = item.label
                                role = Role.Checkbox
                                selected = item.key in highlighted
                                stateDescription = selectionStateLabel(item.key in highlighted)
                                collectionItemInfo = CollectionItemInfo(
                                    rowIndex = index,
                                    rowSpan = 1,
                                    columnIndex = 0,
                                    columnSpan = 1,
                                )
                            },
                    )
                }
            }
        }
    }
}

/** Metadata supplied by a platform file picker to [StylishUpload]. */
@Immutable
public data class StylishUploadFile(public val name: String, public val sizeBytes: Long, public val mimeType: String? = null)

/** Platform-neutral upload queue. A platform adapter supplies selected files. */
@Composable
public fun StylishUpload(
    files: List<StylishUploadFile>,
    onFilesChange: (List<StylishUploadFile>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "",
    onRequestFiles: (() -> Unit)? = null,
) {
    val strings = StylishTheme.strings
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Button(
            onClick = { onRequestFiles?.invoke() },
            enabled = enabled && onRequestFiles != null,
            modifier = Modifier.testTag("stylish_upload_choose_files"),
        ) { Text(label.ifBlank { strings.chooseFiles }) }
        files.forEach { file ->
            Row(
                Modifier.fillMaxWidth().semantics {
                    contentDescription = buildString {
                        append(file.name)
                        append(", ${strings.fileSizeBytes(file.sizeBytes)}")
                        file.mimeType?.let { append(", $it") }
                    }
                },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(file.name)
                IconButton(
                    onClick = { onFilesChange(files - file) },
                    enabled = enabled,
                    modifier = Modifier.semantics { contentDescription = "${strings.remove}: ${file.name}" },
                ) { Text("×") }
            }
        }
    }
}

@Preview
@Composable
private fun StylishTransferUploadPreview() {
    StylishTransfer(
        available = listOf(StylishTransferItem(1, "One", "One"), StylishTransferItem(2, "Two", "Two")),
        selectedKeys = emptySet(),
        onSelectedKeysChange = {},
    )
}
