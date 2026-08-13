package com.segnities007.stylishui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishCheckbox
import com.segnities007.stylishui.components.atoms.StylishRadioButton
import com.segnities007.stylishui.components.atoms.StylishRangeSlider
import com.segnities007.stylishui.components.atoms.StylishSlider
import com.segnities007.stylishui.components.atoms.StylishSwitch
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.components.organisms.StylishConnectedSegmentedControl
import com.segnities007.stylishui.components.organisms.StylishTabBar

@Composable
internal fun DemoSelection(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Switch / Checkbox / Radio",
        description = "選択系の状態をトグルできます。",
        code = """StylishSwitch(checked = checked, onCheckedChange = { checked = it })
StylishCheckbox(checked = checked, onCheckedChange = { checked = it })
StylishRadioButton(selected = selected, onClick = { selected = true })""",
        modifier = modifier,
    ) {
        var switchChecked by remember { mutableStateOf(true) }
        var checkboxChecked by remember { mutableStateOf(false) }
        var radioSelected by remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            StylishSwitch(checked = switchChecked, onCheckedChange = { switchChecked = it })
            StylishCheckbox(checked = checkboxChecked, onCheckedChange = { checkboxChecked = it })
            StylishRadioButton(selected = radioSelected, onClick = { radioSelected = true })
        }
    }

    StylishDemoCard(
        title = "Slider / Range slider",
        description = "値をドラッグして変更できます。",
        code = """StylishSlider(value = value, onValueChange = { value = it })
StylishRangeSlider(value = range, onValueChange = { range = it })""",
        modifier = modifier,
    ) {
        var value by remember { mutableFloatStateOf(0.5f) }
        var range by remember { mutableStateOf(0.2f..0.8f) }
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("${(value * 100).toInt()}%")
        }
        StylishSlider(value = value, onValueChange = { value = it })
        StylishRangeSlider(value = range, onValueChange = { range = it })
    }

    StylishDemoCard(
        title = "Segmented control",
        description = "単一選択のセグメントコントロール。",
        code = """StylishConnectedSegmentedControl(
    options = listOf(
        StylishSegmentedOption("a", "A"),
        StylishSegmentedOption("b", "B"),
        StylishSegmentedOption("c", "C"),
    ),
    selectedValue = selected,
    onSelectedChange = { selected = it },
)""",
        modifier = modifier,
    ) {
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
    }

    StylishDemoCard(
        title = "Tab bar",
        description = "タブの切り替え。",
        code = """StylishTabBar(
    tabs = listOf("概要", "記録", "統計"),
    selectedIndex = index,
    onSelectedChange = { index = it },
)""",
        modifier = modifier,
    ) {
        var selectedTab by remember { mutableIntStateOf(0) }
        StylishTabBar(
            tabs = listOf("概要", "記録", "統計"),
            selectedIndex = selectedTab,
            onSelectedChange = { selectedTab = it },
        )
    }
}
