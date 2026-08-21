package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.isStylishReducedMotionEnabled
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * A horizontal row of dots indicating the current page in a paged
 * container (e.g. a pager). The active dot uses [activeSize] and
 * [activeColor]; inactive dots use [inactiveSize] and [inactiveColor].
 * Size changes are animated so the active dot smoothly grows or
 * shrinks as the page changes.
 *
 * @param pageCount The total number of pages (dots to render). Must be
 *   at least 1.
 * @param currentPage The zero-based index of the currently active page.
 *   Clamped to `0..pageCount - 1`.
 * @param modifier Modifier applied to the outer [Row].
 * @param activeSize Diameter of the active dot. Defaults to 8.dp.
 * @param inactiveSize Diameter of inactive dots. Defaults to 6.dp.
 * @param spacing Gap between adjacent dots. Defaults to 4.dp.
 * @param activeColor Color of the active dot. Defaults to
 *   [MaterialTheme.colorScheme.onSurface].
 * @param inactiveColor Color of inactive dots. Defaults to
 *   [MaterialTheme.colorScheme.outlineVariant].
 */
@Composable
public fun StylishDotIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeSize: Dp = 8.dp,
    inactiveSize: Dp = 6.dp,
    spacing: Dp = 4.dp,
    activeColor: Color = MaterialTheme.colorScheme.onSurface,
    inactiveColor: Color = MaterialTheme.colorScheme.outlineVariant,
) {
    require(pageCount >= 1) { "pageCount must be at least 1, was $pageCount" }
    val clampedPage = currentPage.coerceIn(0, pageCount - 1)
    val strings = StylishTheme.strings
    val animate = !isStylishReducedMotionEnabled()
    Row(
        modifier = modifier.stylishTestTag("dot_indicator").semantics {
            contentDescription = strings.pageOf(clampedPage + 1, pageCount)
        },
        horizontalArrangement = Arrangement.spacedBy(spacing),
    ) {
        repeat(pageCount) { index ->
            val isActive = index == clampedPage
            val size by animateDpAsState(
                targetValue = if (isActive) activeSize else inactiveSize,
                animationSpec = if (animate) {
                    androidx.compose.animation.core.spring()
                } else {
                    androidx.compose.animation.core.snap()
                },
                label = "DotSize",
            )
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(if (isActive) activeColor else inactiveColor),
            )
        }
    }
}

@Preview(name = "Dot indicator first page", showBackground = true, widthDp = 393)
@Composable
private fun StylishDotIndicatorFirstPagePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishDotIndicator(pageCount = 5, currentPage = 0)
        }
    }
}

@Preview(name = "Dot indicator middle page", showBackground = true, widthDp = 393)
@Composable
private fun StylishDotIndicatorMiddlePagePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishDotIndicator(pageCount = 5, currentPage = 2)
        }
    }
}

@Preview(name = "Dot indicator last page", showBackground = true, widthDp = 393)
@Composable
private fun StylishDotIndicatorLastPagePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishDotIndicator(pageCount = 5, currentPage = 4)
        }
    }
}

@Preview(name = "Dot indicator single page", showBackground = true, widthDp = 393)
@Composable
private fun StylishDotIndicatorSinglePagePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishDotIndicator(pageCount = 1, currentPage = 0)
        }
    }
}
