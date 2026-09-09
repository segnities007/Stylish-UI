package com.segnities007.stylishui.components.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.snap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.stylishTestTag
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.theme.StylishElevationLayer
import com.segnities007.stylishui.theme.stylishFloatingContainerColor

/**
 * Displays the current page as a floating pill over paged content.
 *
 * The selected marker expands to 20.dp while inactive markers remain 8.dp,
 * with an 8.dp gap between markers. The pill uses 16.dp horizontal and
 * 10.dp vertical padding, the shared floating surface background, the theme
 * outline, and the primary color for the selected marker. Width and color
 * changes animate with the same motion as StylishMemo's floating pager
 * indicator. When the platform requests reduced motion, both changes snap to
 * their target values.
 *
 * Use this floating variant when the indicator is layered above content. For
 * a compact indicator that participates in normal layout without a surface,
 * use [StylishDotIndicator] instead.
 *
 * @param pageCount The total number of pages to represent. Must be at least 1.
 * @param currentPage The zero-based selected page. Values outside the valid
 *   range are clamped to the first or last page.
 * @param modifier Modifier applied to the outer floating pill.
 * @param onPageSelected Optional callback that makes each marker an accessible
 *   page selector. When `null`, the indicator remains display-only.
 */
@Composable
public fun StylishFloatingPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    onPageSelected: ((Int) -> Unit)? = null,
) {
    require(pageCount >= 1) { "pageCount must be at least 1, was $pageCount" }
    val clampedPage = currentPage.coerceIn(0, pageCount - 1)
    val strings = StylishTheme.strings
    val reducedMotion = isStylishReducedMotionEnabled()

    Surface(
        modifier = modifier
            .stylishTestTag("floating_pager_indicator")
            .semantics {
                contentDescription = strings.pageOf(clampedPage + 1, pageCount)
            },
        shape = RoundedCornerShape(percent = 50),
        color = stylishFloatingContainerColor(),
        border = BorderStroke(
            StylishTheme.dimensions.outlineWidth,
            MaterialTheme.colorScheme.outlineVariant,
        ),
    ) {
        StylishElevationLayer {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(pageCount) { index ->
                    val selected = index == clampedPage
                    val width by animateDpAsState(
                        targetValue = if (selected) 20.dp else 8.dp,
                        animationSpec = if (reducedMotion) {
                            snap()
                        } else {
                            spring(dampingRatio = 0.7f, stiffness = 380f)
                        },
                        label = "floatingPagerIndicatorWidth",
                    )
                    val color by animateColorAsState(
                        targetValue = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                        },
                        animationSpec = if (reducedMotion) {
                            snap()
                        } else {
                            spring()
                        },
                        label = "floatingPagerIndicatorColor",
                    )
                    Box(
                        modifier = Modifier
                            .size(width = width, height = 8.dp)
                            .then(
                                if (onPageSelected == null) {
                                    Modifier
                                } else {
                                    Modifier
                                        .clickable { if (index != clampedPage) onPageSelected(index) }
                                        .semantics {
                                            contentDescription = strings.pageOf(index + 1, pageCount)
                                            this.selected = selected
                                            role = Role.Tab
                                        }
                                },
                            )
                            .background(color, CircleShape),
                    )
                }
            }
        }
    }
}

@Preview(name = "Floating pager indicator", showBackground = true, widthDp = 393)
@Composable
private fun StylishFloatingPagerIndicatorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishFloatingPagerIndicator(pageCount = 5, currentPage = 2)
        }
    }
}
