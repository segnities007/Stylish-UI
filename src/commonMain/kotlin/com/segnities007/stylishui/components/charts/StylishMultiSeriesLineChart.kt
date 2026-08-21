package com.segnities007.stylishui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.foundation.focusable
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.theme.StylishTheme
import com.segnities007.stylishui.foundation.headless.StylishReducer
import kotlin.math.abs
import kotlin.math.roundToInt

/** One named line in [StylishMultiSeriesLineChart]. */
@Immutable
public data class StylishLineSeries(
    public val name: String,
    public val values: List<Float>,
    public val color: Color,
)

/** A selected point, addressed by series and category indexes. */
@Immutable
public data class StylishChartSelection(public val seriesIndex: Int, public val pointIndex: Int)

/** Hoisted interaction state for [StylishMultiSeriesLineChart]. */
@Immutable
public data class StylishChartState(
    public val selection: StylishChartSelection? = null,
    public val visibleSeriesNames: Set<String> = emptySet(),
)

/** Pure actions accepted by [StylishChartState.reduce]. */
@Immutable
public sealed interface StylishChartAction {
    @Immutable
    public data class Select(public val selection: StylishChartSelection?) : StylishChartAction

    @Immutable
    public data class SetVisibleSeries(public val names: Set<String>) : StylishChartAction

    @Immutable
    public data class ToggleSeries(public val name: String) : StylishChartAction
}

/** Shared pure reducer used by Compose, SwiftUI, Web, and desktop host stores. */
public object StylishChartStateReducer : StylishReducer<StylishChartState, StylishChartAction> {
    override fun reduce(state: StylishChartState, action: StylishChartAction): StylishChartState = when (action) {
        is StylishChartAction.Select -> state.copy(selection = action.selection)
        is StylishChartAction.SetVisibleSeries -> state.copy(visibleSeriesNames = action.names)
        is StylishChartAction.ToggleSeries -> state.copy(
            visibleSeriesNames = if (action.name in state.visibleSeriesNames) {
                state.visibleSeriesNames - action.name
            } else {
                state.visibleSeriesNames + action.name
            },
        )
    }
}

/** Returns the next chart interaction state without coupling state to Compose. */
public fun StylishChartState.reduce(action: StylishChartAction): StylishChartState =
    StylishChartStateReducer.reduce(this, action)

private data class ChartSeriesEntry(val originalIndex: Int, val series: StylishLineSeries)

/**
 * Multi-series line chart with a horizontally scrollable legend and tap selection tooltip.
 * Non-finite values are skipped. Series may be shorter than [labels]; missing points are empty.
 */
@Composable
@NonRestartableComposable
public fun StylishMultiSeriesLineChart(
    labels: List<String>,
    series: List<StylishLineSeries>,
    contentDescriptionPrefix: String,
    modifier: Modifier = Modifier,
    selection: StylishChartSelection? = null,
    onSelectionChange: ((StylishChartSelection?) -> Unit)? = null,
    chartHeight: Dp = StylishTheme.dimensions.lineChartHeight,
    gridColor: Color = MaterialTheme.colorScheme.outlineVariant,
    strokeWidth: Dp = 2.5.dp,
    showLegend: Boolean = true,
    tooltip: @Composable ((label: String, series: StylishLineSeries, value: Float) -> Unit)? = null,
    visibleSeriesNames: Set<String> = series.map { it.name }.toSet(),
    onVisibleSeriesNamesChange: ((Set<String>) -> Unit)? = null,
    xAxisLabel: String? = null,
    yAxisLabel: String? = null,
    valueFormatter: ((Float) -> String)? = null,
    yAxisTickCount: Int = 5,
    showAxisTicks: Boolean = false,
    xAxisTickFormatter: (String) -> String = { it },
    yAxisTickFormatter: (Float) -> String = { valueFormatter?.invoke(it) ?: it.toString() },
) {
    val displayedSeries = series.mapIndexedNotNull { index, line ->
        if (line.name in visibleSeriesNames) ChartSeriesEntry(index, line) else null
    }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val finiteValues = displayedSeries.flatMap { it.series.values }.filter { it.isFinite() }
    val min = finiteValues.minOrNull()?.coerceAtMost(0f) ?: 0f
    val max = finiteValues.maxOrNull()?.coerceAtLeast(0f) ?: 1f
    val range = (max - min).takeIf { it > 0f } ?: 1f
    val description = buildString {
        append(contentDescriptionPrefix)
        displayedSeries.forEach { entry ->
            append(". ${entry.series.name}: ")
            append(entry.series.values.mapIndexedNotNull { index, value ->
                if (!value.isFinite()) null
                else "${xAxisTickFormatter(labels.getOrElse(index) { index.toString() })}=${valueFormatter?.invoke(value) ?: value}"
            }.joinToString())
        }
    }
    val firstPoint = displayedSeries.asSequence().flatMap { entry ->
        entry.series.values.asSequence().mapIndexedNotNull { index, value ->
            if (index < labels.size && value.isFinite()) StylishChartSelection(entry.originalIndex, index) else null
        }
    }.firstOrNull()
    val selectedPoint = selection ?: firstPoint ?: StylishChartSelection(0, 0)
    val density = LocalDensity.current

    Column(modifier.testTag("stylish_multi_series_line_chart")) {
        if (showLegend) {
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                series.forEach { line ->
                    Spacer(Modifier.size(10.dp).background(line.color))
                    Text(
                        line.name,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .padding(start = 4.dp, end = 16.dp)
                            .then(if (onVisibleSeriesNamesChange != null) Modifier.clickable {
                                onVisibleSeriesNamesChange(
                                    if (line.name in visibleSeriesNames) visibleSeriesNames - line.name else visibleSeriesNames + line.name,
                                )
                            } else Modifier)
                            .semantics { selected = line.name in visibleSeriesNames },
                    )
                }
            }
        }
        if (yAxisLabel != null) Text(yAxisLabel, style = MaterialTheme.typography.labelSmall)
        if (showAxisTicks) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween) {
                Text(yAxisTickFormatter(max), style = MaterialTheme.typography.labelSmall)
                Text(yAxisTickFormatter(min), style = MaterialTheme.typography.labelSmall)
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(chartHeight)
                .onSizeChanged { canvasSize = it },
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                    .testTag("stylish_multi_series_line_chart_canvas")
                    .pointerInput(labels, series, onSelectionChange, canvasSize) {
                    detectTapGestures { tap ->
                        if (onSelectionChange == null || labels.isEmpty() || canvasSize.width == 0) return@detectTapGestures
                        val pointIndex = ((tap.x / canvasSize.width) * (labels.size - 1).coerceAtLeast(1))
                            .roundToInt().coerceIn(labels.indices)
                        val normalizedY = 1f - tap.y / canvasSize.height
                        val tappedValue = min + normalizedY * range
                        val entry = displayedSeries
                            .filter { pointIndex < it.series.values.size && it.series.values[pointIndex].isFinite() }
                            .minByOrNull { abs(it.series.values[pointIndex] - tappedValue) }
                        onSelectionChange(entry?.let { StylishChartSelection(it.originalIndex, pointIndex) })
                    }
                    }
                    .focusable()
                    .onPreviewKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown || onSelectionChange == null || labels.isEmpty() || displayedSeries.isEmpty()) return@onPreviewKeyEvent false
                        val currentEntryIndex = displayedSeries.indexOfFirst { it.originalIndex == selectedPoint.seriesIndex }
                            .let { if (it >= 0) it else 0 }
                        val currentEntry = displayedSeries[currentEntryIndex]
                        val currentPoint = selectedPoint.pointIndex.coerceIn(0, labels.lastIndex)
                        val pointIndexes = currentEntry.series.values.mapIndexedNotNull { index, value ->
                            if (index < labels.size && value.isFinite()) index else null
                        }
                        if (pointIndexes.isEmpty()) return@onPreviewKeyEvent false
                        val next = when (event.key) {
                            Key.DirectionLeft -> StylishChartSelection(
                                currentEntry.originalIndex,
                                pointIndexes.lastOrNull { it < currentPoint } ?: pointIndexes.first(),
                            )
                            Key.DirectionRight -> StylishChartSelection(
                                currentEntry.originalIndex,
                                pointIndexes.firstOrNull { it > currentPoint } ?: pointIndexes.last(),
                            )
                            Key.MoveHome -> StylishChartSelection(currentEntry.originalIndex, pointIndexes.first())
                            Key.MoveEnd -> StylishChartSelection(currentEntry.originalIndex, pointIndexes.last())
                            Key.DirectionUp, Key.DirectionDown -> {
                                val direction = if (event.key == Key.DirectionUp) -1 else 1
                                val candidateIndexes = generateSequence(currentEntryIndex + direction) { it + direction }
                                    .takeWhile { it in displayedSeries.indices }
                                val nextEntry = candidateIndexes
                                    .map { displayedSeries[it] }
                                    .firstOrNull { it.series.values.getOrNull(currentPoint)?.isFinite() == true }
                                    ?: return@onPreviewKeyEvent false
                                StylishChartSelection(nextEntry.originalIndex, currentPoint)
                            }
                            else -> return@onPreviewKeyEvent false
                        }
                        if (next != selection) onSelectionChange(next)
                        true
                    }
                    .semantics {
                        contentDescription = description
                        val selectedLine = selection?.let { series.getOrNull(it.seriesIndex) }
                        val selectedValue = selection?.let { selectedLine?.values?.getOrNull(it.pointIndex) }
                        if (selectedLine != null && selectedValue != null && selectedValue.isFinite()) {
                            stateDescription = "${xAxisTickFormatter(labels.getOrNull(selection.pointIndex).orEmpty())}: ${selectedLine.name}=${valueFormatter?.invoke(selectedValue) ?: selectedValue}"
                        }
                    },
            ) {
            val tickCount = yAxisTickCount.coerceAtLeast(2)
            repeat(tickCount) { index ->
                val y = size.height * index / (tickCount - 1).toFloat()
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), 1f)
            }
            displayedSeries.forEach { entry ->
                val line = entry.series
                val originalSeriesIndex = entry.originalIndex
                val path = Path()
                var started = false
                line.values.take(labels.size).forEachIndexed { index, value ->
                    if (!value.isFinite()) {
                        started = false
                    } else {
                        val x = if (labels.size <= 1) size.width / 2f else size.width * index / (labels.size - 1)
                        val y = size.height * (1f - (value - min) / range)
                        if (started) path.lineTo(x, y) else path.moveTo(x, y)
                        started = true
                        if (selection == StylishChartSelection(originalSeriesIndex, index)) {
                            drawCircle(line.color, radius = 6.dp.toPx(), center = Offset(x, y))
                        }
                    }
                }
                drawPath(path, line.color, style = Stroke(strokeWidth.toPx()))
            }
            }
            // Expose each finite point as an individual accessibility node. The
            // canvas remains the keyboard surface; point nodes provide virtual
            // cursor semantics and a direct activation target for touch users.
            if (canvasSize.width > 0 && canvasSize.height > 0) {
                displayedSeries.forEach { entry ->
                    entry.series.values.take(labels.size).forEachIndexed { index, value ->
                        if (!value.isFinite()) return@forEachIndexed
                        val x = if (labels.size <= 1) canvasSize.width / 2f else canvasSize.width.toFloat() * index / (labels.size - 1)
                        val y = canvasSize.height * (1f - (value - min) / range)
                        val pointLabel = "${xAxisTickFormatter(labels.getOrElse(index) { index.toString() })}: ${entry.series.name}=${valueFormatter?.invoke(value) ?: value}"
                        val pointModifier = Modifier
                            .offset(
                                x = (x / density.density).dp - 12.dp,
                                y = (y / density.density).dp - 12.dp,
                            )
                            .size(24.dp)
                            .semantics {
                                contentDescription = pointLabel
                                selected = selection == StylishChartSelection(entry.originalIndex, index)
                                stateDescription = pointLabel
                            }
                        Box(
                            if (onSelectionChange != null) pointModifier.clickable {
                                onSelectionChange(StylishChartSelection(entry.originalIndex, index))
                            } else pointModifier,
                        )
                    }
                }
            }
        }
        if (xAxisLabel != null) {
            Text(xAxisLabel, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
        }
        selection?.let { selected ->
            val line = series.getOrNull(selected.seriesIndex)
            val value = line?.values?.getOrNull(selected.pointIndex)
            val label = labels.getOrNull(selected.pointIndex)
            if (line != null && value != null && label != null) {
                Box(Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    if (tooltip != null) tooltip(label, line, value)
                    else Surface(color = MaterialTheme.colorScheme.inverseSurface) {
                        Text("$label · ${line.name}: $value", color = MaterialTheme.colorScheme.inverseOnSurface, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * Controlled overload using [StylishChartState]. The reducer-backed state can be
 * persisted or shared with a store while the renderer remains the same.
 */
@Composable
@NonRestartableComposable
public fun StylishMultiSeriesLineChart(
    labels: List<String>,
    series: List<StylishLineSeries>,
    contentDescriptionPrefix: String,
    state: StylishChartState,
    onStateChange: (StylishChartState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visibleNames = state.visibleSeriesNames.ifEmpty { series.map { it.name }.toSet() }
    StylishMultiSeriesLineChart(
        labels = labels,
        series = series,
        contentDescriptionPrefix = contentDescriptionPrefix,
        modifier = modifier,
        selection = state.selection,
        onSelectionChange = { selection -> onStateChange(state.copy(selection = selection)) },
        visibleSeriesNames = visibleNames,
        onVisibleSeriesNamesChange = { names -> onStateChange(state.copy(visibleSeriesNames = names)) },
    )
}

@Preview(name = "Multi-series line chart", widthDp = 500, heightDp = 360)
@Composable
private fun StylishMultiSeriesLineChartPreview() {
    StylishTheme(darkTheme = false) {
        StylishMultiSeriesLineChart(
            labels = listOf("Jan", "Feb", "Mar", "Apr"),
            series = listOf(
                StylishLineSeries("Actual", listOf(10f, 18f, 15f, 24f), MaterialTheme.colorScheme.primary),
                StylishLineSeries("Target", listOf(12f, 14f, 18f, 20f), MaterialTheme.colorScheme.tertiary),
            ),
            contentDescriptionPrefix = "Monthly performance",
            selection = StylishChartSelection(0, 2),
        )
    }
}
