package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishCheckbox
import com.segnities007.stylishui.components.atoms.StylishRadioButton
import com.segnities007.stylishui.components.atoms.StylishRangeSlider
import com.segnities007.stylishui.components.atoms.StylishSlider
import com.segnities007.stylishui.components.atoms.StylishSwitch
import com.segnities007.stylishui.components.atoms.StylishText
import com.segnities007.stylishui.components.atoms.StylishTriStateCheckbox
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.components.organisms.StylishConnectedSegmentedControl
import com.segnities007.stylishui.components.organisms.StylishTabBar
import androidx.compose.ui.state.ToggleableState

/**
 * Returns all selection-related demo components for the catalog.
 */
internal fun getSelectionDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Switch / Checkbox / Radio",
        category = DemoCategory.Selection,
        code = """StylishSwitch(checked = checked, onCheckedChange = { checked = it })
StylishCheckbox(checked = checked, onCheckedChange = { checked = it })
StylishRadioButton(selected = selected, onClick = { selected = true })
StylishSwitch(checked = true, onCheckedChange = null, enabled = false)
StylishCheckbox(checked = false, onCheckedChange = null, enabled = false)""",
        preview = {
            var switchChecked by remember { mutableStateOf(true) }
            var checkboxChecked by remember { mutableStateOf(false) }
            var radioSelected by remember { mutableStateOf(false) }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StylishSwitch(checked = switchChecked, onCheckedChange = { switchChecked = it })
                StylishCheckbox(checked = checkboxChecked, onCheckedChange = { checkboxChecked = it })
                StylishRadioButton(selected = radioSelected, onClick = { radioSelected = true })
                StylishSwitch(checked = true, onCheckedChange = null, enabled = false)
                StylishCheckbox(checked = false, onCheckedChange = null, enabled = false)
            }
        },
    ),
    DemoComponent(
        name = "Slider / Range slider",
        category = DemoCategory.Selection,
        code = """StylishSlider(value = value, onValueChange = { value = it })
StylishRangeSlider(value = range, onValueChange = { range = it })""",
        preview = {
            var value by remember { mutableFloatStateOf(0.5f) }
            var range by remember { mutableStateOf(0.2f..0.8f) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                StylishText("${(value * 100).toInt()}%")
            }
            StylishSlider(value = value, onValueChange = { value = it })
            StylishRangeSlider(value = range, onValueChange = { range = it })
        },
    ),
    DemoComponent(
        name = "Segmented control",
        category = DemoCategory.Selection,
        code = """StylishConnectedSegmentedControl(
    options = listOf(
        StylishSegmentedOption("a", "A"),
        StylishSegmentedOption("b", "B"),
        StylishSegmentedOption("c", "C"),
    ),
    selectedValue = selected,
    onSelectedChange = { selected = it },
)""",
        preview = {
            var selected by remember { mutableStateOf("b") }
            StylishConnectedSegmentedControl(
                options = listOf(
                    StylishSegmentedOption("a", "概要"),
                    StylishSegmentedOption("b", "記録"),
                    StylishSegmentedOption("c", "統計"),
                ),
                selectedValue = selected,
                onSelectedChange = { selected = it },
            )
        },
    ),
    DemoComponent(
        name = "Tab bar",
        category = DemoCategory.Selection,
        code = """StylishTabBar(
    tabs = listOf("A", "B", "C", "D", "E"),
    selectedIndex = index,
    onSelectedChange = { index = it },
)""",
        preview = {
            var selectedTab by remember { mutableIntStateOf(0) }
            StylishTabBar(
                tabs = listOf("ホーム", "ダッシュボード", "レポート", "設定", "ヘルプ"),
                selectedIndex = selectedTab,
                onSelectedChange = { selectedTab = it },
            )
        },
    ),
)
