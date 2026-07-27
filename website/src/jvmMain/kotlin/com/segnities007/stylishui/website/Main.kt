package com.segnities007.stylishui.website

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.segnities007.stylishui.components.atoms.StylishFab
import com.segnities007.stylishui.components.atoms.StylishIconButton
import com.segnities007.stylishui.components.atoms.StylishRoundedIconButton
import com.segnities007.stylishui.components.charts.PieChartData
import com.segnities007.stylishui.components.charts.SimplePieChart
import com.segnities007.stylishui.components.charts.stylishChartColor
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishConnectedChipItem
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.components.atoms.StylishDialogSurface
import com.segnities007.stylishui.components.atoms.StylishFormTextField
import com.segnities007.stylishui.components.atoms.StylishSectionTitle
import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow
import com.segnities007.stylishui.components.molecules.StylishConnectedCardGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedChipColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedChipGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedChipRow
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemGrid
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemRow
import com.segnities007.stylishui.components.molecules.StylishDatePickerField
import com.segnities007.stylishui.components.molecules.StylishEmptyState
import com.segnities007.stylishui.components.organisms.StylishConnectedSegmentedControl
import com.segnities007.stylishui.components.organisms.StylishDeleteConfirmDialog
import com.segnities007.stylishui.components.organisms.StylishDialogActions
import com.segnities007.stylishui.components.patterns.StylishHeader
import com.segnities007.stylishui.components.patterns.StylishPageContent
import com.segnities007.stylishui.theme.StylishTheme
import kotlinx.datetime.LocalDate

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Stylish UI - Official Website",
    ) {
        StylishTheme(darkTheme = false) {
            Surface(Modifier.fillMaxSize()) {
                WebsiteContent()
            }
        }
    }
}

@Composable
fun WebsiteContent() {
    StylishPageContent(
        header = {
            StylishHeader(
                title = {
                    Column {
                        Text(
                            text = "Stylish UI",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            text = "Compose Multiplatform design system",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    StylishIconButton(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        onClick = {},
                    )
                    StylishRoundedIconButton(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        onClick = {},
                    )
                },
            )
        },
    ) {
        item { HeroSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { SegmentedSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { CardsSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { ButtonsSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { ChipsSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { ListSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { ChartSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { FormSection() }
        item { Spacer(Modifier.height(24.dp)) }
        item { DialogSection() }
        item { Spacer(Modifier.height(32.dp)) }
        item { EmptyStateSection() }
        item { Spacer(Modifier.height(32.dp)) }
        item { FooterSection() }
    }
}

@Composable
private fun HeroSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Build beautiful, accessible UIs",
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = "Stylish UI provides a semantic-first design system for Android, JVM Desktop, and Web.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        StylishFab(
            imageVector = Icons.Default.Add,
            contentDescription = "Get started",
            onClick = {},
        )
    }
}

@Composable
private fun SegmentedSection() {
    StylishSectionTitle(title = "Segmented Control")
    Spacer(Modifier.height(12.dp))
    var selected by remember { mutableStateOf("day") }
    StylishConnectedSegmentedControl(
        options = listOf(
            StylishSegmentedOption("day", "Day"),
            StylishSegmentedOption("week", "Week"),
            StylishSegmentedOption("month", "Month"),
        ),
        selectedValue = selected,
        onSelected = { selected = it },
    )
}

@Composable
private fun CardsSection() {
    StylishSectionTitle(title = "Connected Cards")
    Spacer(Modifier.height(12.dp))
    StylishConnectedCardGrid(
        items = listOf(
            StylishConnectedCardItem(
                title = "Actionable",
                supportingText = "Click and elevation",
                onClick = {},
            ),
            StylishConnectedCardItem(
                title = "Read only",
                supportingText = "No click or elevation",
            ),
        ),
        columns = 2,
    )
}

@Composable
private fun ButtonsSection() {
    StylishSectionTitle(title = "Connected Buttons")
    Spacer(Modifier.height(12.dp))
    StylishConnectedButtonRow(
        items = listOf(
            StylishConnectedButtonItem(onClick = {}) { Text("Primary") },
            StylishConnectedButtonItem(onClick = {}) { Text("Secondary") },
        ),
    )
    Spacer(Modifier.height(16.dp))
    StylishConnectedButtonRow(
        items = listOf(
            StylishConnectedButtonItem(
                onClick = {},
                enabled = false,
            ) { Text("Disabled") },
            StylishConnectedButtonItem(onClick = {}) { Text("Enabled") },
        ),
    )
}

@Composable
private fun ChipsSection() {
    StylishSectionTitle(title = "Connected Chips")
    Spacer(Modifier.height(12.dp))

    Text("Row", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(8.dp))
    StylishConnectedChipRow(
        items = listOf(
            StylishConnectedChipItem("Selected", {}, selected = true),
            StylishConnectedChipItem("Read only"),
        ),
    )

    Spacer(Modifier.height(16.dp))
    Text("Column", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(8.dp))
    StylishConnectedChipColumn(
        items = listOf(
            StylishConnectedChipItem("Selected", {}, selected = true),
            StylishConnectedChipItem("Read only"),
        ),
    )

    Spacer(Modifier.height(16.dp))
    Text("Grid", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(8.dp))
    StylishConnectedChipGrid(
        items = listOf(
            StylishConnectedChipItem("A", {}, selected = true),
            StylishConnectedChipItem("B", {}),
            StylishConnectedChipItem("C", {}),
        ),
        columns = 2,
    )
}

@Composable
private fun ListSection() {
    StylishSectionTitle(title = "Connected List Items")
    Spacer(Modifier.height(12.dp))

    Text("Column", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(8.dp))
    StylishConnectedListItemColumn(
        items = listOf(
            StylishConnectedListItem("Actionable item", onClick = {}),
            StylishConnectedListItem("Read-only item"),
            StylishConnectedListItem("Disabled item", enabled = false),
        ),
    )

    Spacer(Modifier.height(16.dp))
    Text("Row", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(8.dp))
    StylishConnectedListItemRow(
        items = listOf(
            StylishConnectedListItem("Theme", supportingText = "System", onClick = {}),
            StylishConnectedListItem("Notifications", supportingText = "On", onClick = {}),
        ),
    )

    Spacer(Modifier.height(16.dp))
    Text("Grid", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(Modifier.height(8.dp))
    StylishConnectedListItemGrid(
        items = listOf(
            StylishConnectedListItem("Theme", supportingText = "System", onClick = {}),
            StylishConnectedListItem("Notifications", supportingText = "On", onClick = {}),
            StylishConnectedListItem("General", supportingText = "Language", onClick = {}),
        ),
        columns = 2,
    )
}

@Composable
private fun ChartSection() {
    StylishSectionTitle(title = "Charts")
    Spacer(Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SimplePieChart(
            contentDescriptionPrefix = "Pie chart",
            data = listOf(
                PieChartData("Fuel", 35f, stylishChartColor(0)),
                PieChartData("Insurance", 25f, stylishChartColor(1)),
                PieChartData("Maintenance", 20f, stylishChartColor(2)),
                PieChartData("Other", 20f, stylishChartColor(3)),
            ),
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Pie chart", style = MaterialTheme.typography.titleMedium)
            Text("Simple pie chart from commonMain, works on all targets including Web.")
        }
    }
}

@Composable
private fun FormSection() {
    StylishSectionTitle(title = "Form")
    Spacer(Modifier.height(12.dp))
    val text = remember { mutableStateOf("") }
    StylishFormTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = "Your name",
        placeholder = "Enter your name",
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(16.dp))
    val date = remember { mutableStateOf<LocalDate?>(null) }
    StylishDatePickerField(
        value = date.value,
        onValueChange = { date.value = it },
        label = "Date",
        placeholder = "Select date",
        confirmLabel = "OK",
        dismissLabel = "Cancel",
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DialogSection() {
    StylishSectionTitle(title = "Dialogs")
    Spacer(Modifier.height(12.dp))
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        StylishDialogSurface(onDismiss = { showDialog = false }) {
            Column(Modifier.padding(24.dp)) {
                Text("Dialog surface", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))
                Text("This is a dialog surface used to build custom dialogs.")
                Spacer(Modifier.height(24.dp))
                StylishDialogActions(
                    confirmLabel = "OK",
                    cancelLabel = "Cancel",
                    onConfirm = { showDialog = false },
                    onCancel = { showDialog = false },
                )
            }
        }
    }

    if (showDeleteDialog) {
        StylishDeleteConfirmDialog(
            title = "Delete item",
            message = "Are you sure you want to delete this item?",
            confirmLabel = "Delete",
            cancelLabel = "Cancel",
            onConfirm = { showDeleteDialog = false },
            onDismiss = { showDeleteDialog = false },
        )
    }

    StylishConnectedButtonRow(
        items = listOf(
            StylishConnectedButtonItem(onClick = { showDialog = true }) { Text("Show dialog") },
            StylishConnectedButtonItem(onClick = { showDeleteDialog = true }) { Text("Show delete dialog") },
        ),
    )
}

@Composable
private fun EmptyStateSection() {
    StylishSectionTitle(title = "Empty State")
    Spacer(Modifier.height(12.dp))
    StylishEmptyState(
        icon = Icons.Default.Search,
        title = "No data",
        description = "There is no data to display.",
        actionLabel = "Refresh",
        onAction = {},
    )
}

@Composable
private fun FooterSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HorizontalDivider()
        Text(
            text = "Apache License 2.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "github.com/segnities007/Stylish-UI",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
private fun WebsiteContentPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.fillMaxSize()) {
            WebsiteContent()
        }
    }
}
