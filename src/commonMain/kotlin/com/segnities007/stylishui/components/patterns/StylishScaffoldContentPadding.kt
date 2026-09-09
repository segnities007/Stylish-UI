package com.segnities007.stylishui.components.patterns

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Bottom clearance published by the nearest [StylishScaffold].
 *
 * The value is a contract for scrollable content, not a modifier for the
 * scaffold root. A [LazyColumn] should consume it through
 * [stylishScaffoldContentPadding], so its last item can scroll above a
 * floating surface without shrinking the screen viewport.
 * Nested scaffolds inherit the largest required clearance.
 */
public val LocalStylishScaffoldContentPadding: ProvidableCompositionLocal<PaddingValues> =
    staticCompositionLocalOf<PaddingValues> { PaddingValues() }

/**
 * Merges caller padding with the nearest scaffold's bottom clearance.
 *
 * The largest bottom value wins because the scaffold value already includes
 * all outer scaffold requirements. Start, top, and end values remain owned by
 * the caller.
 */
@Composable
public fun stylishScaffoldContentPadding(
    contentPadding: PaddingValues = PaddingValues(),
): PaddingValues {
    val scaffoldPadding = LocalStylishScaffoldContentPadding.current
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection),
        top = contentPadding.calculateTopPadding(),
        end = contentPadding.calculateEndPadding(layoutDirection),
        bottom = maxOf(
            contentPadding.calculateBottomPadding(),
            scaffoldPadding.calculateBottomPadding(),
        ),
    )
}

/** Returns only the nearest scaffold's bottom clearance for scroll modifiers. */
@Composable
public fun stylishScaffoldBottomPadding(): Dp =
    LocalStylishScaffoldContentPadding.current.calculateBottomPadding()

/** Theme-independent defaults for the scaffold's standard floating slots. */
public object StylishScaffoldDefaults {
    /** Extra clearance for a regular FAB placed above the navigation bar. */
    public val floatingActionButtonContentPadding: Dp = 88.dp
}

@Preview(name = "Scaffold content padding", showBackground = true)
@Composable
private fun StylishScaffoldContentPaddingPreview() {
    Text(
        text = "Content consumes scaffold clearance",
        modifier = androidx.compose.ui.Modifier.padding(
            bottom = stylishScaffoldContentPadding().calculateBottomPadding(),
        ),
    )
}
