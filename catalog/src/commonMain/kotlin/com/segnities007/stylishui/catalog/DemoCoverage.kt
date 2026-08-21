package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.segnities007.stylishui.components.atoms.StylishConnectedCard
import com.segnities007.stylishui.components.atoms.StylishDialogSurface
import com.segnities007.stylishui.components.atoms.StylishExposedDropdownMenu
import com.segnities007.stylishui.components.atoms.StylishExposedDropdownMenuBox
import com.segnities007.stylishui.components.atoms.StylishFilledIconButton
import com.segnities007.stylishui.components.atoms.StylishFilledTonalIconButton
import com.segnities007.stylishui.components.atoms.StylishOutlinedIconButton
import com.segnities007.stylishui.components.atoms.StylishSecureTextField
import com.segnities007.stylishui.components.atoms.StylishSectionTitle
import com.segnities007.stylishui.components.atoms.StylishSpeedDial
import com.segnities007.stylishui.components.atoms.SpeedDialDirection
import com.segnities007.stylishui.components.charts.StylishAreaChart
import com.segnities007.stylishui.components.charts.StylishAreaPoint
import com.segnities007.stylishui.components.charts.StylishScatterChart
import com.segnities007.stylishui.components.charts.StylishScatterPoint
import com.segnities007.stylishui.components.models.StylishContentState
import com.segnities007.stylishui.components.molecules.StylishButtonGroup
import com.segnities007.stylishui.components.molecules.StylishContentStateHost
import com.segnities007.stylishui.components.molecules.StylishListItem
import com.segnities007.stylishui.components.molecules.StylishMasonry
import com.segnities007.stylishui.components.molecules.StylishPullToRefresh
import com.segnities007.stylishui.components.molecules.StylishSkeletonAvatar
import com.segnities007.stylishui.components.molecules.StylishSkeletonLine
import com.segnities007.stylishui.components.molecules.StylishSnackbar
import com.segnities007.stylishui.components.molecules.StylishSplitter
import com.segnities007.stylishui.components.molecules.StylishSwipeToDismissBox
import com.segnities007.stylishui.components.molecules.StylishToolbar
import com.segnities007.stylishui.components.molecules.rememberStylishSwipeToDismissBoxState
import com.segnities007.stylishui.components.molecules.StylishTimePickerDialog
import com.segnities007.stylishui.components.organisms.StylishCommandItem
import com.segnities007.stylishui.components.organisms.StylishCommandPalette
import com.segnities007.stylishui.components.organisms.StylishDialogActions
import com.segnities007.stylishui.components.organisms.StylishHoverCard
import com.segnities007.stylishui.components.organisms.StylishScrollArea
import com.segnities007.stylishui.components.organisms.StylishSingleChoiceSegmentedButtonRow
import com.segnities007.stylishui.components.organisms.StylishSegmentedButton
import com.segnities007.stylishui.components.organisms.StylishWideNavigationRail
import com.segnities007.stylishui.theme.StylishTheme

/**
 * Coverage demos for public APIs that are useful on their own but are not
 * represented by one of the themed family demos.  Each entry intentionally
 * invokes the public API in its preview; the copy-ready snippet is not a
 * name-only alias.  Platform-dependent behavior (for example file pickers)
 * remains in the adapter demos.
 */
internal fun getCoverageDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Connected card anatomy",
        category = DemoCategory.Buttons,
        code = """StylishConnectedCard(
    title = \"Long text card title\",
    supportingText = \"Supporting text\",
    enabled = enabled,
    onClick = { /* select */ },
)""",
        preview = {
            StylishConnectedCard(
                title = "Long text card title",
                supportingText = "Supporting text",
                enabled = true,
                onClick = {},
            )
        },
    ),
    DemoComponent(
        name = "Dialog surface",
        category = DemoCategory.Buttons,
        code = """StylishDialogSurface(
    onDismiss = { open = false },
    properties = DialogProperties(dismissOnBackPress = true),
    content = { Text(\"Dialog content\") },
)""",
        preview = {
            StylishDialogSurface(
                onDismiss = {},
                animate = false,
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            ) {
                Text("Dialog content", Modifier.padding(24.dp))
            }
        },
    ),
    DemoComponent(
        name = "Exposed dropdown menu",
        category = DemoCategory.Buttons,
        code = """StylishExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = it },
) {
    TextField(value = value, onValueChange = {}, readOnly = true)
    StylishExposedDropdownMenu(expanded, onDismissRequest = { expanded = false }) {
        StylishDropdownMenuItem(text = { Text(\"Option\") }, onClick = { expanded = false })
    }
}""",
        preview = { exposedDropdownCoveragePreview() },
    ),
    DemoComponent(
        name = "Icon button variants",
        category = DemoCategory.Buttons,
        code = """StylishFilledIconButton(onClick = onClick) { Icon(Icons.Default.Add, \"Add\") }
StylishFilledTonalIconButton(onClick = onClick) { Icon(Icons.Default.Settings, \"Settings\") }
StylishOutlinedIconButton(onClick = onClick) { Icon(Icons.Default.Delete, \"Delete\") }""",
        preview = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishFilledIconButton(onClick = {}) { Icon(Icons.Default.Add, "Add") }
                StylishFilledTonalIconButton(onClick = {}) { Icon(Icons.Default.Settings, "Settings") }
                StylishOutlinedIconButton(onClick = {}) { Icon(Icons.Default.Delete, "Delete") }
            }
        },
    ),
    DemoComponent(
        name = "Section title",
        category = DemoCategory.Buttons,
        code = """StylishSectionTitle(
    title = \"Account settings\",
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
)""",
        preview = { StylishSectionTitle("Account settings") },
    ),
    DemoComponent(
        name = "Secure text field",
        category = DemoCategory.Inputs,
        code = """val state = remember { TextFieldState(\"secret\") }
StylishSecureTextField(
    state = state,
    label = { Text(\"Password\") },
    isError = showError,
    enabled = enabled,
)""",
        preview = { secureTextFieldCoveragePreview() },
    ),
    DemoComponent(
        name = "Speed dial actions",
        category = DemoCategory.Buttons,
        code = """StylishSpeedDial(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    direction = SpeedDialDirection.Up,
    actionCount = 2,
    onActionClick = { index -> onAction(index) },
) { index -> TextButton(onClick = { onActionClick(index) }) { Text(\"Action \${'$'}index\") } }""",
        preview = { speedDialCoveragePreview() },
    ),
    DemoComponent(
        name = "Area chart",
        category = DemoCategory.Charts,
        code = """StylishAreaChart(
    points = listOf(StylishAreaPoint(\"Jan\", 12f), StylishAreaPoint(\"Feb\", 22f)),
    contentDescription = \"Revenue trend\",
)""",
        preview = {
            StylishAreaChart(
                points = listOf(StylishAreaPoint("Jan", 12f), StylishAreaPoint("Feb", 22f), StylishAreaPoint("Mar", 18f)),
                contentDescription = "Revenue trend",
            )
        },
    ),
    DemoComponent(
        name = "Scatter chart",
        category = DemoCategory.Charts,
        code = """StylishScatterChart(
    points = points,
    contentDescription = \"Measurements\",
    pointRadius = 5.dp,
)""",
        preview = {
            StylishScatterChart(
                points = listOf(StylishScatterPoint("A", 10f), StylishScatterPoint("B", 24f), StylishScatterPoint("C", 16f)),
                contentDescription = "Measurements",
            )
        },
    ),
    DemoComponent(
        name = "Button group slots",
        category = DemoCategory.WebParity,
        code = """StylishButtonGroup(
    orientation = StylishButtonGroupOrientation.Horizontal,
    spacing = 8.dp,
) {
    Button(onClick = onPrimary) { Text(\"Primary\") }
    OutlinedButton(onClick = onSecondary) { Text(\"Secondary\") }
}""",
        preview = {
            StylishButtonGroup {
                Button(onClick = {}) { Text("Primary") }
                Button(onClick = {}) { Text("Secondary") }
            }
        },
    ),
    DemoComponent(
        name = "Content state host",
        category = DemoCategory.WebParity,
        code = """StylishContentStateHost(
    state = state, // Loading / Empty / Error / Content
    loadingContent = { Text(\"Loading…\") },
    emptyContent = { Text(\"No data\") },
    errorContent = { error -> Text(error.message) },
) { value -> Text(value) }""",
        preview = { contentStateCoveragePreview() },
    ),
    DemoComponent(
        name = "List item slots",
        category = DemoCategory.WebParity,
        code = """StylishListItem(
    headline = \"Workspace\",
    supportingText = \"Long supporting text\",
    leadingContent = { Icon(Icons.Default.Home, \"Workspace\") },
    trailingContent = { Icon(Icons.Default.Settings, \"Settings\") },
    onClick = onClick,
)""",
        preview = {
            StylishListItem(
                headline = "Workspace",
                supportingText = "Long supporting text",
                leadingContent = { Icon(Icons.Default.Home, "Workspace") },
                trailingContent = { Icon(Icons.Default.Settings, "Settings") },
                onClick = {},
            )
        },
    ),
    DemoComponent(
        name = "Masonry layout",
        category = DemoCategory.WebParity,
        code = """StylishMasonry(itemCount = items.size, columns = 2) { index ->
    Card { Text(items[index], Modifier.padding(16.dp)) }
}""",
        preview = {
            StylishMasonry(itemCount = 6, columns = 2) { index ->
                StylishListItem(headline = "Tile $index", supportingText = "Masonry content")
            }
        },
    ),
    DemoComponent(
        name = "Pull to refresh",
        category = DemoCategory.WebParity,
        code = """StylishPullToRefresh(
    isRefreshing = isRefreshing,
    onRefresh = { refresh() },
) { ListContent() }""",
        preview = { pullToRefreshCoveragePreview() },
    ),
    DemoComponent(
        name = "Skeleton loading",
        category = DemoCategory.Feedback,
        code = """StylishSkeletonAvatar(animate = isLoading)
StylishSkeletonLine(Modifier.width(180.dp).height(16.dp), animate = isLoading)""",
        preview = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishSkeletonAvatar(animate = false)
                StylishSkeletonLine(Modifier.width(180.dp).size(16.dp), animate = false)
                StylishSkeletonLine(Modifier.width(120.dp).size(12.dp), animate = false)
            }
        },
    ),
    DemoComponent(
        name = "Slot snackbar",
        category = DemoCategory.Feedback,
        code = """StylishSnackbar(
    action = { TextButton(onClick = onUndo) { Text(\"Undo\") } },
    dismissAction = { IconButton(onClick = onDismiss) { Icon(Icons.Default.Delete, \"Dismiss\") } },
) { Text(\"Saved\") }""",
        preview = {
            StylishSnackbar(
                action = { TextButton(onClick = {}) { Text("Undo") } },
                dismissAction = { IconButton(onClick = {}) { Icon(Icons.Default.Delete, "Dismiss") } },
            ) { Text("Saved", Modifier.padding(horizontal = 12.dp)) }
        },
    ),
    DemoComponent(
        name = "Resizable splitter",
        category = DemoCategory.WebParity,
        code = """StylishSplitter(
    ratio = ratio,
    onRatioChange = { ratio = it },
    first = { FirstPane() },
    second = { SecondPane() },
)""",
        preview = {
            StylishSplitter(
                modifier = Modifier.size(width = 320.dp, height = 120.dp),
                ratio = 0.5f,
                first = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("First") } },
                second = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Second") } },
            )
        },
    ),
    DemoComponent(
        name = "Swipe to dismiss",
        category = DemoCategory.WebParity,
        code = """val state = rememberStylishSwipeToDismissBoxState()
StylishSwipeToDismissBox(
    state = state,
    backgroundContent = { Text(\"Delete\") },
    onDismiss = { value -> onDismiss(value) },
) { ListItem() }""",
        preview = { swipeToDismissCoveragePreview() },
    ),
    DemoComponent(
        name = "Toolbar slots",
        category = DemoCategory.Navigation,
        code = """StylishToolbar(
    title = \"Workspace\",
    subtitle = \"3 items\",
    navigationContent = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, \"Back\") } },
    actions = { IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, \"Settings\") } },
)""",
        preview = {
            StylishToolbar(
                title = "Workspace",
                subtitle = "3 items",
                navigationContent = { IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, "Back") } },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.Settings, "Settings") } },
            )
        },
    ),
    DemoComponent(
        name = "Dialog actions",
        category = DemoCategory.Advanced,
        code = """StylishDialogActions(
    confirmLabel = \"Save\",
    cancelLabel = \"Cancel\",
    confirmEnabled = canSave,
    onConfirm = onSave,
    onCancel = onCancel,
)""",
        preview = {
            StylishDialogActions(
                confirmLabel = "Save",
                cancelLabel = "Cancel",
                onConfirm = {},
                onCancel = {},
            )
        },
    ),
    DemoComponent(
        name = "Hover card",
        category = DemoCategory.Advanced,
        code = """StylishHoverCard(
    trigger = { Text(\"Hover for details\") },
    content = { Text(\"Additional context\") },
)""",
        preview = {
            StylishHoverCard(
                trigger = { Text("Hover for details") },
            ) { Text("Additional context", Modifier.padding(16.dp)) }
        },
    ),
    DemoComponent(
        name = "Scroll area",
        category = DemoCategory.Advanced,
        code = """StylishScrollArea {
    items.forEach { Text(it) }
}""",
        preview = {
            StylishScrollArea(Modifier.size(width = 260.dp, height = 100.dp)) {
                repeat(8) { Text("Scrollable item ${it + 1}", Modifier.padding(8.dp)) }
            }
        },
    ),
    DemoComponent(
        name = "Wide navigation rail",
        category = DemoCategory.Navigation,
        code = """StylishWideNavigationRail {
    StylishNavigationRailItem(
        selected = selected,
        onClick = onSelect,
        icon = { Icon(Icons.Default.Home, \"Home\") },
        label = { Text(\"Home\") },
    )
}""",
        preview = {
            StylishWideNavigationRail {
                androidx.compose.material3.NavigationRailItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") },
                )
            }
        },
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun exposedDropdownCoveragePreview() {
    var expanded by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("Category") }
    StylishExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        androidx.compose.material3.TextField(
            value = value,
            onValueChange = { value = it },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Category") },
        )
        StylishExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            androidx.compose.material3.DropdownMenuItem(
                text = { Text("Design") },
                onClick = { value = "Design"; expanded = false },
            )
            androidx.compose.material3.DropdownMenuItem(
                text = { Text("Engineering") },
                onClick = { value = "Engineering"; expanded = false },
            )
        }
    }
}

@Composable
private fun secureTextFieldCoveragePreview() {
    val state = remember { androidx.compose.foundation.text.input.TextFieldState("secret") }
    StylishSecureTextField(state = state, label = { Text("Password") }, isError = false)
}

@Composable
private fun speedDialCoveragePreview() {
    var expanded by remember { mutableStateOf(true) }
    StylishSpeedDial(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        direction = SpeedDialDirection.Up,
        actionCount = 2,
        onActionClick = {},
    ) { index ->
        androidx.compose.material3.FloatingActionButton(onClick = {}) {
            Text((index + 1).toString())
        }
    }
}

@Composable
private fun contentStateCoveragePreview() {
    var state by remember { mutableStateOf<StylishContentState<String>>(StylishContentState.Loading) }
    StylishContentStateHost(
        state = state,
        loadingContent = { Text("Loading…", Modifier.padding(16.dp)) },
        emptyContent = { Text(it ?: "No data", Modifier.padding(16.dp)) },
        errorContent = { Text("Error: ${it.message}", Modifier.padding(16.dp)) },
    ) { value ->
        Text(value, Modifier.padding(16.dp))
    }
    // Keep all four branches copy-visible without changing the rendered default.
    state = state
}

@Composable
private fun pullToRefreshCoveragePreview() {
    var refreshing by remember { mutableStateOf(false) }
    StylishPullToRefresh(
        isRefreshing = refreshing,
        onRefresh = { refreshing = true },
        modifier = Modifier.size(width = 280.dp, height = 120.dp),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(if (refreshing) "Loading…" else "Pull to refresh")
            Text("Empty and error states belong to the host")
        }
    }
}

@Composable
private fun swipeToDismissCoveragePreview() {
    val state = rememberStylishSwipeToDismissBoxState()
    StylishSwipeToDismissBox(
        state = state,
        backgroundContent = { Text("Delete", Modifier.padding(16.dp)) },
        modifier = Modifier.fillMaxWidth(),
        onDismiss = {},
    ) {
        StylishListItem(headline = "Swipe me", supportingText = "Dismissible row")
    }
}
