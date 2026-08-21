package com.segnities007.stylishui.performance

import com.segnities007.stylishui.components.charts.StylishChartMaxRenderedPoints
import com.segnities007.stylishui.components.charts.downsampleStylishSeries
import com.segnities007.stylishui.components.organisms.StylishDataTableColumn
import com.segnities007.stylishui.components.organisms.StylishDataTableSortDirection
import com.segnities007.stylishui.components.organisms.StylishDataTableSortState
import com.segnities007.stylishui.components.organisms.StylishTreeNode
import com.segnities007.stylishui.components.organisms.flattenStylishTree
import com.segnities007.stylishui.components.organisms.resolveStylishDataTableRows
import java.io.File
import java.util.Locale
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Linux-friendly performance contract for deterministic, non-rendering workloads.
 *
 * The thresholds are deliberately broad smoke budgets; this is not a frame-time or heap SLO. Each
 * workload is warmed up and measured repeatedly so the report is useful for detecting an algorithmic
 * regression instead of recording one scheduler-dependent sample. Device frame/memory benchmarks remain
 * platform-specific gates.
 */
class PerformanceBudgetTest {
    private companion object {
        /** Keep the protocol short enough for CI while allowing the JVM to warm up. */
        const val WARMUP_ITERATIONS = 2
        const val MEASUREMENT_ITERATIONS = 7
    }

    @Test
    fun largeDataWorkloadsStayWithinAlgorithmicSmokeBudgets() {
        val tableRows = (0 until 10_000).toList()
        val tableColumn = StylishDataTableColumn<Int>(
            id = "value",
            title = "Value",
            comparator = compareBy { it },
            cell = {},
        )
        val tableSort = {
            resolveStylishDataTableRows(
                rows = tableRows,
                columns = listOf(tableColumn),
                sortStates = listOf(
                    StylishDataTableSortState("value", StylishDataTableSortDirection.Descending),
                ),
                page = 20,
                pageSize = 50,
            )
        }
        val tableResult = tableSort()
        assertEquals(50, tableResult.visibleRows.size)

        val treeNodes = (0 until 100_000).map { StylishTreeNode(it, "Node $it", Unit) }
        val treeFlatten = { flattenStylishTree(treeNodes, emptySet()) }
        val treeResult = treeFlatten()
        assertEquals(100_000, treeResult.size)

        val chartSource = (0 until 100_000).map { it.toFloat() }
        val chartDownsample = {
            downsampleStylishSeries(chartSource, StylishChartMaxRenderedPoints)
        }
        val chartResult = chartDownsample()
        assertEquals(StylishChartMaxRenderedPoints, chartResult.size)

        val measurements = listOf(
            measure("dataTable-10k-sort", 5_000L, tableSort),
            measure("tree-100k-flatten", 5_000L, treeFlatten),
            measure("chart-100k-downsample", 2_000L, chartDownsample),
        )
        measurements.forEach { measurement ->
            val millis = measurement.p95Millis
            assertTrue(
                millis <= measurement.budgetMillis,
                "${measurement.name} p95 exceeded ${measurement.budgetMillis}ms: ${millis}ms",
            )
        }
        writeReportIfRequested(measurements)
    }

    private fun <T : Any> measure(name: String, budgetMillis: Long, block: () -> T): Measurement {
        repeat(WARMUP_ITERATIONS) { block() }
        val samples = LongArray(MEASUREMENT_ITERATIONS) { measureNanoTime { block() } }
        return Measurement(name, samples, budgetMillis)
    }

    private data class Measurement(
        val name: String,
        val samplesNanos: LongArray,
        val budgetMillis: Long,
    ) {
        private val sortedSamplesNanos: LongArray get() = samplesNanos.copyOf().apply { sort() }
        val minMillis: Double get() = samplesNanos.minOrNull()!!.toMillis()
        val medianMillis: Double get() = sortedSamplesNanos[sortedSamplesNanos.lastIndex / 2].toMillis()
        val p95Millis: Double get() = sortedSamplesNanos[((sortedSamplesNanos.size * 95 + 99) / 100 - 1)
            .coerceIn(0, sortedSamplesNanos.lastIndex)].toMillis()
        val samplesMillis: List<Double> get() = samplesNanos.map { it.toMillis() }

        private fun Long.toMillis(): Double = this / 1_000_000.0
    }

    private fun writeReportIfRequested(measurements: List<Measurement>) {
        if (System.getenv("WRITE_PERFORMANCE_REPORT") != "1") return
        val report = File("build/reports/performance/algorithmic-budgets.json")
        val javaVersion = jsonString(System.getProperty("java.version"))
        val osName = jsonString(System.getProperty("os.name"))
        val osArch = jsonString(System.getProperty("os.arch"))
        val revision = jsonString(System.getenv("GITHUB_SHA") ?: "local")
        report.parentFile.mkdirs()
        report.writeText(
            buildString {
                appendLine("{")
                appendLine("  \"schemaVersion\": 1,")
                appendLine("  \"scope\": \"Linux JVM deterministic algorithmic smoke\",")
                appendLine("  \"not_a_frame_or_heap_slo\": true,")
                appendLine("  \"protocol\": {\"warmupIterations\":$WARMUP_ITERATIONS,\"measurementIterations\":$MEASUREMENT_ITERATIONS,\"statistic\":\"p95Millis\",\"budgetRule\":\"p95Millis <= budgetMillis\"},")
                appendLine("  \"environment\": {")
                appendLine("    \"javaVersion\": $javaVersion,")
                appendLine("    \"os\": $osName,")
                appendLine("    \"arch\": $osArch,")
                appendLine("    \"revision\": $revision")
                appendLine("  },")
                appendLine("  \"measurements\": [")
                measurements.forEachIndexed { index, measurement ->
                    fun formatted(value: Double) = String.format(Locale.US, "%.3f", value)
                    append("    {\"name\":${jsonString(measurement.name)},\"unit\":\"ms\",")
                    append("\"samplesMillis\":[${measurement.samplesMillis.joinToString(",") { formatted(it) }}],")
                    append("\"minMillis\":${formatted(measurement.minMillis)},")
                    append("\"medianMillis\":${formatted(measurement.medianMillis)},")
                    append("\"p95Millis\":${formatted(measurement.p95Millis)},")
                    append("\"budgetMillis\":${measurement.budgetMillis},\"status\":\"PASS\"}")
                    if (index != measurements.lastIndex) append(',')
                    appendLine()
                }
                appendLine("  ]")
                appendLine("}")
            },
        )
    }

    private fun jsonString(value: String): String =
        "\"${value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")}\""
}
