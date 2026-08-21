package com.segnities007.stylishui.components.molecules

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.stylishTestTag

/**
 * Layout direction of a [StylishSplitter].
 *
 * @property Horizontal Panels are laid out left/right with a vertical
 *   drag handle.
 * @property Vertical Panels are laid out top/bottom with a horizontal
 *   drag handle.
 */
public enum class StylishSplitterDirection { Horizontal, Vertical }

/**
 * A draggable split panel — the web "Splitter/Resizable" pattern from
 * Ant Design and Radix UI.
 *
 * Lays [first] and [second] out side by side (or stacked) with a drag
 * handle between them. Dragging the handle resizes the panels within
 * [minRatio]..[maxRatio] of the total size. The split ratio is managed
 * internally; hoist it via [ratio]/[onRatioChange] to control it from
 * the caller.
 *
 * @param first Content of the first panel.
 * @param second Content of the second panel.
 * @param modifier Modifier applied to the root container.
 * @param direction Whether the panels split horizontally or vertically.
 * @param ratio Current split ratio (0..1) of the first panel. Defaults
 *   to 0.5. When [onRatioChange] is null, this is the initial value and
 *   the component manages the ratio internally.
 * @param onRatioChange Optional callback invoked while dragging. When
 *   null, the component manages the ratio internally.
 * @param minRatio Minimum fraction of the first panel. Defaults to 0.1.
 * @param maxRatio Maximum fraction of the first panel. Defaults to 0.9.
 * @param handleSize Thickness of the drag handle. Defaults to 8 dp.
 * @param handleColor Color of the handle at rest. Defaults to
 *   [MaterialTheme.colorScheme.outlineVariant].
 * @param handleActiveColor Color of the handle while dragging. Defaults
 *   to [MaterialTheme.colorScheme.primary].
 * @param keyboardStep Fraction to move per arrow-key press. Defaults to
 *   `0.05` (five percent).
 * @param handleContentDescription Accessible label announced for the
 *   focusable resize handle.
 */
@Composable
public fun StylishSplitter(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    direction: StylishSplitterDirection = StylishSplitterDirection.Horizontal,
    ratio: Float = 0.5f,
    onRatioChange: ((Float) -> Unit)? = null,
    minRatio: Float = 0.1f,
    maxRatio: Float = 0.9f,
    handleSize: Dp = 8.dp,
    handleColor: Color = MaterialTheme.colorScheme.outlineVariant,
    handleActiveColor: Color = MaterialTheme.colorScheme.primary,
    keyboardStep: Float = 0.05f,
    handleContentDescription: String = "Resize panels",
) {
    // A malformed bound must never produce a negative/zero weight. This is especially
    // important for values loaded from persisted responsive-layout preferences.
    val safeMinRatio = minRatio.takeIf { it.isFinite() }?.coerceIn(0f, 1f) ?: 0.1f
    val safeMaxRatio = maxRatio.takeIf { it.isFinite() }
        ?.coerceIn(safeMinRatio, 1f)
        ?: 0.9f.coerceAtLeast(safeMinRatio)
    val safeKeyboardStep = keyboardStep.takeIf { it.isFinite() }?.coerceIn(0.001f, 1f) ?: 0.05f
    var internalRatio by remember {
        mutableFloatStateOf(ratio.takeIf { it.isFinite() }?.coerceIn(safeMinRatio, safeMaxRatio) ?: 0.5f)
    }
    val resolvedRatio = (onRatioChange?.let { ratio } ?: internalRatio)
        .takeIf { it.isFinite() }
        ?.coerceIn(safeMinRatio, safeMaxRatio)
        ?: ((safeMinRatio + safeMaxRatio) / 2f)
    var dragging by remember { mutableStateOf(false) }

    val density = androidx.compose.ui.platform.LocalDensity.current
    BoxWithConstraints(modifier = modifier.stylishTestTag("splitter").fillMaxSize()) {
        val totalSize = if (direction == StylishSplitterDirection.Horizontal) {
            maxWidth
        } else {
            maxHeight
        }

        fun updateRatioByDelta(deltaPx: Float) {
            val total = with(density) { totalSize.toPx() }
            if (total <= 0f) return
            val next = (resolvedRatio + deltaPx / total).coerceIn(safeMinRatio, safeMaxRatio)
            onRatioChange?.invoke(next) ?: run { internalRatio = next }
        }

        fun updateRatio(nextRatio: Float) {
            val next = nextRatio.takeIf { it.isFinite() }?.coerceIn(safeMinRatio, safeMaxRatio)
                ?: resolvedRatio
            onRatioChange?.invoke(next) ?: run { internalRatio = next }
        }

        // Keep pointer input alive while the latest ratio/callback is replaced. Using
        // pointerInput(Unit) with a directly captured ratio would otherwise drag from a
        // stale value after the first pointer move.
        val latestUpdateRatioByDelta = rememberUpdatedState<(Float) -> Unit>(
            newValue = { deltaPx: Float -> updateRatioByDelta(deltaPx) },
        )
        val handleModifier = Modifier
            .focusable()
            .semantics {
                contentDescription = handleContentDescription
                stateDescription = "${(resolvedRatio * 100f).toInt()}%"
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = resolvedRatio,
                    range = safeMinRatio..safeMaxRatio,
                )
                setProgress { target: Float ->
                    updateRatio(target)
                    true
                }
            }
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                val positiveKey = when (direction) {
                    StylishSplitterDirection.Horizontal -> Key.DirectionRight
                    StylishSplitterDirection.Vertical -> Key.DirectionDown
                }
                val negativeKey = when (direction) {
                    StylishSplitterDirection.Horizontal -> Key.DirectionLeft
                    StylishSplitterDirection.Vertical -> Key.DirectionUp
                }
                when (event.key) {
                    positiveKey -> updateRatio(resolvedRatio + safeKeyboardStep)
                    negativeKey -> updateRatio(resolvedRatio - safeKeyboardStep)
                    Key.MoveHome -> updateRatio(safeMinRatio)
                    Key.MoveEnd -> updateRatio(safeMaxRatio)
                    else -> return@onPreviewKeyEvent false
                }
                true
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { dragging = true },
                    onDragEnd = { dragging = false },
                    onDragCancel = { dragging = false },
                ) { change, dragAmount ->
                    change.consume()
                    latestUpdateRatioByDelta.value(
                        if (direction == StylishSplitterDirection.Horizontal) dragAmount.x else dragAmount.y,
                    )
                }
            }
            .background(if (dragging) handleActiveColor else handleColor)

        if (direction == StylishSplitterDirection.Horizontal) {
            Row(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .weight(resolvedRatio)
                        .fillMaxHeight(),
                ) {
                    first()
                }
                Box(
                    Modifier
                        .width(handleSize)
                        .fillMaxHeight()
                        .then(handleModifier),
                )
                Box(
                    Modifier
                        .weight(1f - resolvedRatio)
                        .fillMaxHeight(),
                ) {
                    second()
                }
            }
        } else {
            // The vertical variant must use a Column: Row weights divide width and make
            // the allegedly vertical splitter render as a horizontal one.
            androidx.compose.foundation.layout.Column(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .weight(resolvedRatio)
                        .fillMaxWidth(),
                ) {
                    first()
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(handleSize)
                        .then(handleModifier),
                )
                Box(
                    Modifier
                        .weight(1f - resolvedRatio)
                        .fillMaxWidth(),
                ) {
                    second()
                }
            }
        }
    }
}

@Preview(name = "Stylish splitter", showBackground = true, widthDp = 393)
@Composable
private fun StylishSplitterPreview() {
    StylishTheme(darkTheme = false) {
        Surface(Modifier.padding(20.dp)) {
            StylishSplitter(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                first = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) { Text("左パネル") }
                },
                second = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center,
                    ) { Text("右パネル") }
                },
            )
        }
    }
}
