package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.connectedRowCorners
import com.segnities007.stylishui.foundation.connectedShape
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.stylishComponentColors
import com.segnities007.stylishui.foundation.stylishInteractiveTarget

/**
 * A pager control for long lists — the web "Pagination" pattern from
 * shadcn/ui, MUI, and Ant Design.
 *
 * The page-number window keeps the first/last [boundaryCount] pages
 * always visible and shows a window of up to `siblingCount * 2 + 1`
 * pages around the current page, collapsing the rest with ellipses.
 *
 * Consecutive page numbers render as one connected strip using the
 * Connected corner geometry; ellipses and chevrons break the joins.
 * Every number or ellipsis occupies a fixed-width slot, and missing
 * positions are blank spacers, so the row width never shifts when the
 * window collapses near the first and last pages.
 *
 * @param page The current page (1-based).
 * @param onPageChange Called with the new page when the user navigates.
 * @param modifier Modifier applied to the root row.
 * @param pageCount Total number of pages. Must be at least 1.
 * @param siblingCount Number of page buttons shown on each side of the
 *   current page before ellipsis collapse. Defaults to 1.
 * @param boundaryCount Number of always-visible pages at the start and
 *   end. Defaults to 1.
 * @param enabled When `false`, all controls are disabled.
 * @param colors [ButtonColors] for the current page button. Defaults to
 *   the primary container.
 * @param unselectedColors [ButtonColors] for other page buttons.
 *   Defaults to the grouped-container look.
 * @param previousPageContentDescription Localized accessibility label for previous-page action.
 * @param nextPageContentDescription Localized accessibility label for next-page action.
 * @param selectedPageStateDescription Localized selected-state description.
 */
@Composable
public fun StylishPagination(
    page: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    pageCount: Int,
    siblingCount: Int = 1,
    boundaryCount: Int = 1,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ),
    unselectedColors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.stylishComponentColors.groupedContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
    previousPageContentDescription: String? = null,
    nextPageContentDescription: String? = null,
    selectedPageStateDescription: String? = null,
) {
    require(pageCount > 0) { "pageCount must be greater than zero" }
    val safePage = page.coerceIn(1, pageCount)
    val strings = StylishTheme.strings
    val middleItems = pageWindow(pageCount, safePage, siblingCount, boundaryCount)
    // Maximum middle-strip slots (numbers plus both ellipses) so the row
    // keeps a constant width whether or not the window collapses.
    val maxMiddleSlots = boundaryCount * 2 + (siblingCount * 2 + 1) + 2

    Row(
        modifier = modifier.stylishTestTag("pagination"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StylishTheme.dimensions.inlineSpacing),
    ) {
        IconButton(
            onClick = { onPageChange(safePage - 1) },
            enabled = enabled && safePage > 1,
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = previousPageContentDescription ?: strings.previousPage)
        }

        middleItems.forEachIndexed { index, item ->
            when (item) {
                is PageItem.Number -> {
                    val selected = item.number == safePage
                    // Position inside its contiguous run so each run reads as
                    // one connected strip; ellipses break the joins.
                    var runStart = index
                    while (runStart > 0 && middleItems[runStart - 1] is PageItem.Number) runStart--
                    var runEnd = index
                    while (runEnd < middleItems.lastIndex && middleItems[runEnd + 1] is PageItem.Number) runEnd++
                    Button(
                        onClick = { onPageChange(item.number) },
                        enabled = enabled,
                        colors = if (selected) colors else unselectedColors,
                        shape = connectedShape(connectedRowCorners(index - runStart, runEnd - runStart + 1)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
                        modifier = Modifier
                            // Fixed slot keeps the row width constant; text
                            // stays centered while the 48 dp interaction
                            // minimum is preserved inside the hit target.
                            .width(PageNumberSlotWidth)
                            .stylishInteractiveTarget()
                            .semantics {
                                role = Role.Button
                                this.selected = selected
                                if (selected) stateDescription = selectedPageStateDescription ?: strings.selectedPage
                            },
                    ) {
                        Text(
                            item.number.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                is PageItem.Ellipsis -> {
                    Box(
                        modifier = Modifier.width(PageNumberSlotWidth),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "…",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        // Pad unused positions so the row width never shifts when ellipses
        // collapse near the first and last pages.
        repeat(maxMiddleSlots - middleItems.size) {
            Spacer(Modifier.width(PageNumberSlotWidth))
        }

        IconButton(
            onClick = { onPageChange(safePage + 1) },
            enabled = enabled && safePage < pageCount,
        ) {
            Icon(Icons.Default.ChevronRight, contentDescription = nextPageContentDescription ?: strings.nextPage)
        }
    }
}

/** Fixed outer slot shared by page numbers, ellipses, and blank padding. */
private val PageNumberSlotWidth = 48.dp

private sealed interface PageItem {
    data class Number(val number: Int) : PageItem
    data object Ellipsis : PageItem
}

/**
 * Computes the visible page window: boundaries, current window, and
 * ellipses in between. Returns a sorted list of page numbers and
 * ellipsis markers.
 */
private fun pageWindow(pageCount: Int, page: Int, siblingCount: Int, boundaryCount: Int): List<PageItem> {
    if (pageCount <= boundaryCount * 2 + siblingCount * 2 + 1) {
        return (1..pageCount).map { PageItem.Number(it) }
    }
    val result = mutableListOf<PageItem>()
    val boundaries = (1..boundaryCount).toMutableSet()
    val windowStart = (page - siblingCount).coerceAtLeast(boundaryCount + 1)
    val windowEnd = (page + siblingCount).coerceAtMost(pageCount - boundaryCount)
    boundaries.addAll(windowStart..windowEnd)
    boundaries.addAll((pageCount - boundaryCount + 1)..pageCount)
    var previous = 0
    for (n in boundaries.sorted()) {
        if (previous > 0 && n - previous > 1) {
            result.add(PageItem.Ellipsis)
        }
        result.add(PageItem.Number(n))
        previous = n
    }
    return result
}

@Preview(name = "Stylish pagination", showBackground = true, widthDp = 393)
@Composable
private fun StylishPaginationPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishPagination(page = 5, onPageChange = {}, pageCount = 20)
        }
    }
}
