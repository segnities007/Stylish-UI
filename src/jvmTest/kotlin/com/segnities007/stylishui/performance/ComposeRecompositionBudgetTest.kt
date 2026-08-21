package com.segnities007.stylishui.performance

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.segnities007.stylishui.theme.StylishTheme
import java.io.File
import java.util.Locale
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Linux JVM Compose harness for update/recomposition regression.
 *
 * This deliberately measures successful composition effects and update wall time on the JVM
 * host. It is a repeatable compiler/runtime signal, not a device frame-time, heap, startup, or
 * production SLO. Android Macrobenchmark and native/browser runtime traces remain required.
 */
@OptIn(ExperimentalTestApi::class)
class ComposeRecompositionBudgetTest {

    @Test
    fun stableProbeUpdatesWithinRecompositionBudget() = runComposeUiTest {
        var successfulCompositions = 0
        val updateMillis = mutableListOf<Double>()

        setContent {
            StylishTheme(darkTheme = false) {
                var value by remember { mutableStateOf(0) }
                SideEffect { successfulCompositions++ }
                Column {
                    Text("value=$value")
                    Button(
                        modifier = Modifier.testTag(PROBE_TAG),
                        onClick = { value++ },
                    ) {
                        Text("update")
                    }
                }
            }
        }
        waitForIdle()
        val initialCompositionCount = successfulCompositions
        repeat(UPDATE_ITERATIONS) {
            val elapsed = measureNanoTime {
                onNodeWithTag(PROBE_TAG).performClick()
                waitForIdle()
            }
            updateMillis += elapsed / 1_000_000.0
        }

        val updateCompositionCount = successfulCompositions - initialCompositionCount
        assertEquals(UPDATE_ITERATIONS, updateCompositionCount)
        assertTrue(
            updateCompositionCount <= UPDATE_ITERATIONS,
            "an update must not trigger more than one successful probe composition",
        )
        assertTrue(updateMillis.all { it >= 0.0 && it.isFinite() })
        writeReportIfRequested(initialCompositionCount, updateCompositionCount, updateMillis)
    }

    private fun writeReportIfRequested(initial: Int, updates: Int, samples: List<Double>) {
        if (System.getenv("WRITE_RECOMPOSITION_REPORT") != "1") return
        val report = File("build/reports/performance/compose-recomposition.json")
        val sorted = samples.sorted()
        fun fmt(value: Double) = String.format(Locale.US, "%.3f", value)
        val p95 = sorted[((sorted.size * 95 + 99) / 100 - 1).coerceIn(0, sorted.lastIndex)]
        report.parentFile.mkdirs()
        report.writeText(
            buildString {
                appendLine("{")
                appendLine("  \"schema\": \"stylish-ui.compose-recomposition.v1\",")
                appendLine("  \"scope\": \"Linux JVM Compose UI test recomposition harness\",")
                appendLine("  \"not_a_frame_or_device_slo\": true,")
                appendLine("  \"protocol\": {\"updateIterations\":$UPDATE_ITERATIONS,\"statistic\":\"p95Millis\",\"countMetric\":\"successfulSideEffect\"},")
                appendLine("  \"initialSuccessfulCompositions\":$initial,")
                appendLine("  \"updateSuccessfulCompositions\":$updates,")
                appendLine("  \"updateMillis\":[${samples.joinToString(",") { fmt(it) }}],")
                appendLine("  \"updateP95Millis\":${fmt(p95)},")
                appendLine("  \"recompositionBudget\":$UPDATE_ITERATIONS,")
                appendLine("  \"status\":\"PASS\"")
                appendLine("}")
            },
        )
    }

    private companion object {
        const val PROBE_TAG = "stylish_recomposition_probe"
        const val UPDATE_ITERATIONS = 5
    }
}
