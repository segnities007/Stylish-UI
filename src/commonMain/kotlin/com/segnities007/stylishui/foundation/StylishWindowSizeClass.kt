package com.segnities007.stylishui.foundation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/** Width buckets shared by phone, tablet, desktop, and web layouts. */
public enum class StylishWindowWidthSizeClass { Compact, Medium, Expanded }

/** Height buckets used when vertical space changes the layout strategy. */
public enum class StylishWindowHeightSizeClass { Compact, Medium, Expanded }

/** Platform-independent window classification derived from available content space. */
@Immutable
public data class StylishWindowSizeClass(
    public val widthSizeClass: StylishWindowWidthSizeClass,
    public val heightSizeClass: StylishWindowHeightSizeClass,
)

/** Customizable width and height boundaries for applications with unusual layouts. */
@Immutable
public data class StylishWindowBreakpoints(
    public val compactWidthMax: Dp = 600.dp,
    public val mediumWidthMax: Dp = 840.dp,
    public val compactHeightMax: Dp = 480.dp,
    public val mediumHeightMax: Dp = 900.dp,
)

/**
 * Classifies the available size using the Material adaptive breakpoints.
 *
 * Width boundaries are 600 dp and 840 dp; height boundaries are 480 dp and 900 dp.
 * Keeping this function pure makes responsive decisions deterministic and testable on every
 * Kotlin target.
 */
public fun calculateStylishWindowSizeClass(width: Dp, height: Dp): StylishWindowSizeClass =
    calculateStylishWindowSizeClass(width, height, StylishWindowBreakpoints())

/** Classifies a window using caller-provided breakpoints. */
public fun calculateStylishWindowSizeClass(
    width: Dp,
    height: Dp,
    breakpoints: StylishWindowBreakpoints,
): StylishWindowSizeClass =
    StylishWindowSizeClass(
        widthSizeClass = when {
            width < breakpoints.compactWidthMax -> StylishWindowWidthSizeClass.Compact
            width < breakpoints.mediumWidthMax -> StylishWindowWidthSizeClass.Medium
            else -> StylishWindowWidthSizeClass.Expanded
        },
        heightSizeClass = when {
            height < breakpoints.compactHeightMax -> StylishWindowHeightSizeClass.Compact
            height < breakpoints.mediumHeightMax -> StylishWindowHeightSizeClass.Medium
            else -> StylishWindowHeightSizeClass.Expanded
        },
    )

/**
 * Selects content from the space actually offered by the parent, without platform APIs.
 * The medium and expanded slots default down to the next smaller layout.
 */
@Composable
public fun StylishAdaptiveLayout(
    compact: @Composable (StylishWindowSizeClass) -> Unit,
    modifier: Modifier = Modifier,
    medium: @Composable (StylishWindowSizeClass) -> Unit = compact,
    expanded: @Composable (StylishWindowSizeClass) -> Unit = medium,
) {
    BoxWithConstraints(modifier) {
        val sizeClass = calculateStylishWindowSizeClass(maxWidth, maxHeight)
        when (sizeClass.widthSizeClass) {
            StylishWindowWidthSizeClass.Compact -> compact(sizeClass)
            StylishWindowWidthSizeClass.Medium -> medium(sizeClass)
            StylishWindowWidthSizeClass.Expanded -> expanded(sizeClass)
        }
    }
}

@Preview(name = "Adaptive layout", widthDp = 700, heightDp = 400)
@Composable
private fun StylishAdaptiveLayoutPreview() {
    StylishTheme(darkTheme = false) {
        Surface {
            StylishAdaptiveLayout(
                modifier = Modifier.fillMaxWidth(),
                compact = { Text("Compact", Modifier.padding(16.dp)) },
                medium = { Text("Medium", Modifier.padding(16.dp)) },
                expanded = { Text("Expanded", Modifier.padding(16.dp)) },
            )
        }
    }
}
