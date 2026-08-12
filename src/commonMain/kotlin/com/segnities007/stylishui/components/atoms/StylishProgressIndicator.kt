package com.segnities007.stylishui.components.atoms

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme

/**
 * A circular progress indicator styled with the Stylish theme.
 * Wraps [CircularProgressIndicator] with theme-aware defaults for
 * color, track color, and stroke cap.
 *
 * When [progress] is `null` (default) the indicator animates
 * indeterminately; when non-null, the determinate variant is shown
 * with the arc filled up to the reported progress.
 *
 * @param modifier Modifier applied to the indicator.
 * @param color Color of the progress arc. Defaults to
 *   [MaterialTheme.colorScheme.primary].
 * @param trackColor Color of the background track. Defaults to
 *   [MaterialTheme.colorScheme.surfaceVariant].
 * @param strokeWidth Thickness of the arc stroke. Defaults to 4.dp.
 * @param strokeCap Cap style for the arc endpoints. Defaults to
 *   [StrokeCap.Round].
 * @param progress Current progress in `0f..1f`, or `null` (default)
 *   for an indeterminate indicator. Values outside the range are
 *   coerced into it by Material.
 *
 * @see StylishLinearProgressIndicator
 */
@Composable
public fun StylishCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeWidth: Dp = 4.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
    progress: (() -> Float)? = null,
) {
    if (progress != null) {
        CircularProgressIndicator(
            progress = progress,
            modifier = modifier,
            color = color,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            strokeCap = strokeCap,
        )
    } else {
        CircularProgressIndicator(
            modifier = modifier,
            color = color,
            trackColor = trackColor,
            strokeWidth = strokeWidth,
            strokeCap = strokeCap,
        )
    }
}

/**
 * A linear progress indicator styled with the Stylish theme.
 * Wraps [LinearProgressIndicator] with theme-aware defaults.
 *
 * When [progress] is `null` (default) the indicator animates
 * indeterminately; when non-null, the determinate variant is shown
 * with the bar filled up to the reported progress.
 *
 * @param modifier Modifier applied to the indicator.
 * @param color Color of the progress bar. Defaults to
 *   [MaterialTheme.colorScheme.primary].
 * @param trackColor Color of the background track. Defaults to
 *   [MaterialTheme.colorScheme.surfaceVariant].
 * @param progress Current progress in `0f..1f`, or `null` (default)
 *   for an indeterminate indicator. Values outside the range are
 *   coerced into it by Material.
 *
 * @see StylishCircularProgressIndicator
 */
@Composable
public fun StylishLinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progress: (() -> Float)? = null,
) {
    if (progress != null) {
        LinearProgressIndicator(
            progress = progress,
            modifier = modifier,
            color = color,
            trackColor = trackColor,
        )
    } else {
        LinearProgressIndicator(
            modifier = modifier,
            color = color,
            trackColor = trackColor,
        )
    }
}

@Preview(name = "Circular progress", showBackground = true, widthDp = 393)
@Composable
private fun StylishCircularProgressIndicatorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishCircularProgressIndicator(Modifier.size(48.dp))
        }
    }
}

@Preview(name = "Circular progress determinate", showBackground = true, widthDp = 393)
@Composable
private fun StylishCircularProgressIndicatorDeterminatePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishCircularProgressIndicator(Modifier.size(48.dp), progress = { 0.65f })
        }
    }
}

@Preview(name = "Linear progress", showBackground = true, widthDp = 393)
@Composable
private fun StylishLinearProgressIndicatorPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishLinearProgressIndicator()
        }
    }
}

@Preview(name = "Linear progress determinate", showBackground = true, widthDp = 393)
@Composable
private fun StylishLinearProgressIndicatorDeterminatePreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishLinearProgressIndicator(progress = { 0.65f })
        }
    }
}