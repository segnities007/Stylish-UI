package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishSectionTitle
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A titled section that groups a heading, optional supporting text, and
 * arbitrary content under a consistent vertical rhythm (Sectioned structure).
 *
 * This is a Finish-layer molecule: it composes the [StylishSectionTitle] atom
 * and spaces the heading, supporting text, and [content] using the Rhythm
 * spacing scale ([StylishTheme.dimensions.contentSpacing] by default). Use it
 * to divide a screen into labelled groups — settings panels, forms, detail
 * screens — so that section structure stays consistent. Separate consecutive
 * sections with [StylishTheme.dimensions.sectionSpacing].
 *
 * @param title Heading text for the section.
 * @param supportingText Optional secondary text rendered below the heading in
 *   a muted style. Omitted when `null`.
 * @param spacing Vertical gap between the heading, supporting text, and
 *   content. Defaults to [StylishTheme.dimensions.contentSpacing] (16 dp).
 * @param titleStyle Typography for the heading. Defaults to
 *   [MaterialTheme.typography.titleMedium].
 * @param titleColor Color for the heading. Defaults to
 *   [MaterialTheme.colorScheme.primary].
 * @param supportingTextStyle Typography for [supportingText]. Defaults to
 *   [MaterialTheme.typography.bodyMedium].
 * @param supportingTextColor Color for [supportingText]. Defaults to
 *   [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param content The section body, rendered below the heading. Receives
 *   [ColumnScope].
 *
 * @see StylishSectionTitle
 */
@Composable
public fun StylishSection(
    title: String,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
    spacing: Dp = StylishTheme.dimensions.contentSpacing,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titleColor: Color = MaterialTheme.colorScheme.primary,
    supportingTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    supportingTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        StylishSectionTitle(
            title = title,
            textStyle = titleStyle,
            color = titleColor,
            verticalPadding = 0.dp,
        )
        if (supportingText != null) {
            Text(
                supportingText,
                style = supportingTextStyle,
                color = supportingTextColor,
            )
        }
        content()
    }
}

@Preview(name = "Stylish section", showBackground = true, widthDp = 393)
@Composable
private fun StylishSectionPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishSection(
                title = "通知",
                supportingText = "受信する通知の種類を選択します",
            ) {
                StylishConnectedListItemColumn(
                    listOf(
                        StylishConnectedListItem("メール", onClick = {}),
                        StylishConnectedListItem("プッシュ通知", onClick = {}),
                    ),
                )
            }
        }
    }
}
