package com.segnities007.stylishui.components.organisms

import com.segnities007.stylishui.components.molecules.StylishConnectedButtonRow

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.models.StylishConnectedButtonItem
import com.segnities007.stylishui.components.models.StylishSegmentedOption
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.stylishComponentColors

/** 連結型のセグメントコントロール。選択肢が少なく排他的な切り替えに使う。 */
@Composable
public fun <T> StylishConnectedSegmentedControl(
    options: List<StylishSegmentedOption<T>>,
    selectedValue: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelMaxLines: Int = 1,
    labelOverflow: TextOverflow = TextOverflow.Ellipsis,
    selectedColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ),
    defaultColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
) {
    StylishConnectedButtonRow(
        items = options.map { option ->
            StylishConnectedButtonItem(
                onClick = { onSelected(option.value) },
                colors = selectedColors.takeIf { option.value == selectedValue },
                leadingContent = option.leadingContent,
            ) {
                Text(
                    option.label,
                    maxLines = labelMaxLines,
                    overflow = labelOverflow,
                )
            }
        },
        modifier = modifier,
        spacing = spacing,
        defaultColors = defaultColors,
    )
}

@Preview(name = "Connected segmented control", showBackground = true, widthDp = 393)
@Composable
private fun StylishConnectedSegmentedControlPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishConnectedSegmentedControl(
                options = listOf(
                    StylishSegmentedOption("list", "リスト") {
                        Icon(Icons.AutoMirrored.Filled.ViewList, null)
                    },
                    StylishSegmentedOption("grid", "グリッド") {
                        Icon(Icons.Default.GridView, null)
                    },
                ),
                selectedValue = "list",
                onSelected = {},
            )
        }
    }
}
