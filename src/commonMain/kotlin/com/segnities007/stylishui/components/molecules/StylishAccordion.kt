package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * One collapsible section of a [StylishAccordion].
 *
 * @property title Header text shown in the section's title bar.
 * @property content Body content revealed while the section is expanded.
 * @property enabled When `false`, the header ignores pointer input and
 *   the section cannot be toggled. Defaults to `true`.
 */
public data class StylishAccordionItem(
    public val title: String,
    public val content: @Composable () -> Unit,
    public val enabled: Boolean = true,
)

/**
 * A vertically stacked set of collapsible sections — the web "accordion"
 * pattern from shadcn/ui, MUI, and Chakra UI.
 *
 * At most one section is expanded at a time. The component is fully
 * hoisted when [onExpandedChange] is provided; otherwise it manages the
 * expanded index internally.
 *
 * @param items The sections to display, in order.
 * @param modifier Modifier applied to the root column.
 * @param expandedIndex Index of the currently expanded section, or
 *   `null` to collapse all. When [onExpandedChange] is `null`, this is
 *   the initial value and the component manages its own state.
 * @param onExpandedChange Optional callback invoked with the newly
 *   expanded index (or `null` when a section is collapsed). When
 *   `null`, the component manages its own expansion state.
 * @param spacing Gap between adjacent sections. Defaults to
 *   [StylishTheme.dimensions.connectedSpacing] (3 dp), keeping the
 *   sections visually grouped.
 * @param shape Corner shape of each section surface. Defaults to
 *   [RoundedCornerShape] with
 *   [StylishTheme.dimensions.connectedCornerRadius].
 * @param containerColor Background of each section. Defaults to
 *   [MaterialTheme.colorScheme.surfaceContainerHigh].
 * @param contentColor Foreground color of the section content.
 * @param titleStyle Typography of the section headers. Defaults to
 *   [MaterialTheme.typography.titleMedium].
 * @param titleMaxLines Maximum lines for [StylishAccordionItem.title].
 * @param titleOverflow Overflow strategy for the title.
 * @param enabled When `false`, no section can be toggled.
 */
@Composable
public fun StylishAccordion(
    items: List<StylishAccordionItem>,
    modifier: Modifier = Modifier,
    expandedIndex: Int? = null,
    onExpandedChange: ((Int?) -> Unit)? = null,
    spacing: Dp = StylishTheme.dimensions.connectedSpacing,
    shape: Shape = RoundedCornerShape(StylishTheme.dimensions.connectedCornerRadius),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    titleMaxLines: Int = 1,
    titleOverflow: TextOverflow = TextOverflow.Ellipsis,
    enabled: Boolean = true,
) {
    var internalExpanded by remember { mutableStateOf(expandedIndex) }
    val resolvedExpandedIndex = if (onExpandedChange != null) expandedIndex else internalExpanded
    val reducedMotion = isStylishReducedMotionEnabled()

    Column(
        modifier = modifier.stylishTestTag("accordion"),
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        items.forEachIndexed { index, item ->
            val isExpanded = resolvedExpandedIndex == index
            val headerClickable = enabled && item.enabled
            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                animationSpec = tween(
                    durationMillis = if (reducedMotion) 0 else StylishTheme.animation.durationShort,
                ),
                label = "accordionArrow",
            )
            Surface(
                shape = shape,
                color = containerColor,
                contentColor = contentColor,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics(mergeDescendants = true) {
                                if (headerClickable) role = Role.Button
                            }
                            .then(
                                if (headerClickable) {
                                    Modifier.clickable {
                                        val next = if (isExpanded) null else index
                                        onExpandedChange?.invoke(next) ?: run {
                                            internalExpanded = next
                                        }
                                    }
                                } else {
                                    Modifier
                                },
                            )
                            .padding(
                                horizontal = StylishTheme.dimensions.controlPadding,
                                vertical = StylishTheme.dimensions.controlVerticalPadding,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
                    ) {
                        Text(
                            item.title,
                            style = titleStyle,
                            maxLines = titleMaxLines,
                            overflow = titleOverflow,
                            modifier = Modifier.weight(1f),
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "折りたたむ" else "展開",
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(rotation),
                        )
                    }
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically(
                            animationSpec = tween(
                                durationMillis = if (reducedMotion) 0 else StylishTheme.animation.durationShort,
                            ),
                        ),
                        exit = shrinkVertically(
                            animationSpec = tween(
                                durationMillis = if (reducedMotion) 0 else StylishTheme.animation.durationShort,
                            ),
                        ),
                    ) {
                        Column(
                            Modifier.padding(
                                start = StylishTheme.dimensions.controlPadding,
                                end = StylishTheme.dimensions.controlPadding,
                                bottom = StylishTheme.dimensions.controlVerticalPadding,
                            ),
                        ) {
                            item.content()
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Stylish accordion", showBackground = true, widthDp = 393)
@Composable
private fun StylishAccordionPreview() {
    StylishTheme(darkTheme = false) {
        androidx.compose.material3.Surface(Modifier.padding(20.dp)) {
            StylishAccordion(
                items = listOf(
                    StylishAccordionItem(
                        title = "基本情報",
                        content = {
                            Text("車両の基本情報をここに表示します。", style = MaterialTheme.typography.bodyMedium)
                        },
                    ),
                    StylishAccordionItem(
                        title = "メンテナンス履歴",
                        content = {
                            Text("整備記録の一覧をここに表示します。", style = MaterialTheme.typography.bodyMedium)
                        },
                    ),
                    StylishAccordionItem(
                        title = "注意事項",
                        enabled = false,
                        content = {
                            Text("このセクションは無効です。", style = MaterialTheme.typography.bodyMedium)
                        },
                    ),
                ),
            )
        }
    }
}
