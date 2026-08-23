package com.segnities007.stylishui.visual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import org.junit.Assume.assumeTrue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.segnities007.stylishui.components.atoms.StylishAvatar
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishButtonVariant
import com.segnities007.stylishui.components.atoms.StylishCard
import com.segnities007.stylishui.components.atoms.StylishChip
import com.segnities007.stylishui.components.atoms.StylishChipVariant
import com.segnities007.stylishui.components.atoms.StylishSlider
import com.segnities007.stylishui.components.charts.BarChartData
import com.segnities007.stylishui.components.charts.LineChartData
import com.segnities007.stylishui.components.charts.PieChartData
import com.segnities007.stylishui.components.charts.SimpleBarChart
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.charts.SimplePieChart
import com.segnities007.stylishui.components.charts.stylishChartColor
import com.segnities007.stylishui.components.models.StylishConnectedCardItem
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.molecules.StylishConnectedCardColumn
import com.segnities007.stylishui.components.molecules.StylishConnectedCardRow
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.theme.StylishTheme
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Golden (pixel-baseline) tests for the Stylish look.
 *
 * Each test renders a fixed scene of representative components inside
 * `StylishTheme` (light and dark) and captures the rendered pixels with
 * `onRoot().captureToImage()`.
 *
 * **Usage**
 * - **First run = recording.** When the baseline PNG does not exist yet, it
 *   is written to `src/jvmTest/resources/golden/<name>-golden.png` and the
 *   test passes. This is how the baseline set is created and extended.
 * - **Subsequent runs = comparison.** The captured pixels are compared
 *   pixel-by-pixel against the baseline. At most 5 % of pixels may differ
 *   (antialiasing jitter + cross-machine font-rendering tolerance);
 *   anything above fails with the diff count in the failure message.
 * - **Updating a baseline**: delete the PNG (or the whole `golden/`
 *   directory) and re-run; the next run records the new baseline.
 *   Treat baseline updates as intentional visual changes and review them
 *   like any other diff.
 *
 * **Determinism**: the scene avoids infinite animations — charts are drawn
 *   with `animate = false` and skeletons are not included. The content is
 *   placed in a fixed-size [Surface] (393 x 900 dp) so the captured image
 *   is exactly the scene, independent of window size.
 */
@OptIn(ExperimentalTestApi::class)
class GoldenTest {

    /**
     * Golden baselines are recorded on the developer machine and compared
     * pixel-by-pixel, but the rendered text depends on the host's fonts
     * (the scene uses Japanese text; CI's Ubuntu image has no CJK fonts and
     * would render tofu glyphs). The comparison is therefore meaningful only
     * in a stable local environment — CI skips these tests.
     */
    private val skipOnCi: Boolean = System.getenv("CI") != null

    @Test
    fun lightThemeGolden() = runComposeUiTest {
        assumeTrue("golden tests are font-environment dependent; run locally", !skipOnCi)
        setContent {
            StylishTheme(darkTheme = false) {
                GoldenScene()
            }
        }
        assertGolden("light")
    }

    @Test
    fun darkThemeGolden() = runComposeUiTest {
        assumeTrue("golden tests are font-environment dependent; run locally", !skipOnCi)
        setContent {
            StylishTheme(darkTheme = true) {
                GoldenScene()
            }
        }
        assertGolden("dark")
    }

    /**
     * The release visual matrix is intentionally explicit. It covers the
     * combinations most likely to reveal layout regressions without relying
     * on an opaque screenshot service: both themes, both directions, the
     * supported narrow width, 200% font scale, and every content state.
     *
     * Baselines are written on the first local run under
     * `src/jvmTest/resources/golden/matrix/`. CI skips pixel comparison when
     * its font environment is not pinned; [verify-visual-matrix.sh] still
     * checks that the full matrix contract remains present in source.
     */
    @Test
    fun visualRegressionMatrixGoldens() = runComposeUiTest {
        assumeTrue("golden tests are font-environment dependent; run locally", !skipOnCi)
        visualMatrix.forEach { scenario ->
            setContent {
                CompositionLocalProvider(
                    LocalLayoutDirection provides scenario.layoutDirection,
                    LocalDensity provides Density(1f, scenario.fontScale),
                ) {
                    StylishTheme(darkTheme = scenario.darkTheme) {
                        MatrixScene(scenario)
                    }
                }
            }
            assertGolden(scenario.id)
        }
    }

    /**
     * A non-rendering guard that makes accidental matrix shrinkage visible in
     * ordinary JVM test reports even when pixel goldens are skipped on CI.
     */
    @Test
    fun visualRegressionMatrixContractIsComplete() {
        assertTrue(visualMatrix.map { it.id }.distinct().size == visualMatrix.size)
        assertTrue(visualMatrix.map { it.darkTheme }.toSet() == setOf(false, true))
        assertTrue(visualMatrix.map { it.layoutDirection }.toSet() ==
            setOf(LayoutDirection.Ltr, LayoutDirection.Rtl))
        assertTrue(visualMatrix.map { it.widthDp }.toSet() == setOf(320, 393))
        assertTrue(visualMatrix.map { it.fontScale }.toSet() == setOf(1f, 2f))
        assertTrue(visualMatrix.map { it.state }.toSet() == VisualState.entries.toSet())
        assertEquals(96, visualMatrix.size)
    }

    private fun ComposeUiTest.assertGolden(name: String) {
        onRoot().assertExists()
        waitForIdle()
        val current = onRoot().captureToImage().toPixelMap()
        val file = baselineFile(name)
        if (!file.exists() || System.getenv("UPDATE_GOLDENS") == "1") {
            file.parentFile?.mkdirs()
            writePng(current, file)
            return
        }
        val baseline = readPng(file)
            ?: error("Baseline PNG is unreadable: ${file.absolutePath}")
        val total = current.width * current.height
        val diffCount = countDifferingPixels(current, baseline)
        val diffRatio = diffCount.toFloat() / total
        assertTrue(
            diffRatio <= TOLERANCE,
            buildString {
                append("Golden image mismatch for '$name': ")
                append("$diffCount of $total pixels differ ")
                append("(${(diffRatio * 100).roundToInt()}% > ${TOLERANCE * 100}%). ")
                append("Delete ${file.absolutePath} and re-run to record a new baseline.")
            },
        )
    }

    private fun baselineFile(name: String): File =
        if ('/' in name) {
            File("src/jvmTest/resources/golden/matrix", "$name-golden.png")
        } else {
            File("src/jvmTest/resources/golden", "$name-golden.png")
        }

    private fun countDifferingPixels(current: PixelMap, baseline: BufferedImage): Int {
        if (current.width != baseline.width || current.height != baseline.height) {
            return current.width * current.height
        }
        var diff = 0
        for (y in 0 until current.height) {
            for (x in 0 until current.width) {
                if (current[x, y].toArgb() != baseline.getRGB(x, y)) diff++
            }
        }
        return diff
    }

    private fun writePng(pixelMap: PixelMap, file: File) {
        val image = BufferedImage(pixelMap.width, pixelMap.height, BufferedImage.TYPE_INT_ARGB)
        for (y in 0 until pixelMap.height) {
            for (x in 0 until pixelMap.width) {
                image.setRGB(x, y, pixelMap[x, y].toArgb())
            }
        }
        check(ImageIO.write(image, "png", file)) { "Failed to write baseline: ${file.absolutePath}" }
    }

    private fun readPng(file: File): BufferedImage? = ImageIO.read(file)

    private companion object {
        const val TOLERANCE = 0.05f

        val visualMatrix: List<VisualScenario> = buildList {
            listOf(false, true).forEach { darkTheme ->
                listOf(LayoutDirection.Ltr, LayoutDirection.Rtl).forEach { direction ->
                    listOf(393, 320).forEach { widthDp ->
                        listOf(1f, 2f).forEach { fontScale ->
                            VisualState.entries.forEach { state ->
                                add(
                                    VisualScenario(
                                        darkTheme = darkTheme,
                                        layoutDirection = direction,
                                        widthDp = widthDp,
                                        fontScale = fontScale,
                                        state = state,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class VisualState {
    Default,
    Disabled,
    Loading,
    Error,
    Empty,
    LongText,
}

private data class VisualScenario(
    val darkTheme: Boolean,
    val layoutDirection: LayoutDirection,
    val widthDp: Int,
    val fontScale: Float,
    val state: VisualState,
) {
    val id: String
        get() = buildString {
            append(if (darkTheme) "dark" else "light")
            append('/').append(if (layoutDirection == LayoutDirection.Rtl) "rtl" else "ltr")
            append('/').append(widthDp).append("dp")
            append('/').append((fontScale * 100).toInt()).append("pct")
            append('/').append(state.name.lowercase())
        }
}

/**
 * The fixed golden scene: one representative instance of each covered
 * component, stacked in a fixed-size surface. Everything here must stay
 * animation-free (or settle deterministically) to keep the baseline stable.
 */
@Composable
private fun GoldenScene() {
    Surface(
        modifier = Modifier.size(393.dp, 900.dp),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StylishButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    variant = StylishButtonVariant.Filled,
                ) {
                    Text("ボタン")
                }
                StylishButton(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    variant = StylishButtonVariant.Tonal,
                ) {
                    Text("トーナル")
                }
            }
            StylishChip(
                label = "フィルタ",
                onClick = {},
                variant = StylishChipVariant.Filter,
                selected = true,
            )
            StylishCard(title = "カードタイトル", supportingText = "補足テキスト")
            Row(verticalAlignment = Alignment.CenterVertically) {
                StylishAvatar(initials = "ST")
                StylishSlider(
                    value = 0.5f,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                )
            }
            StylishConnectedCardRow(
                items = listOf(
                    StylishConnectedCardItem("売上", "¥1,200,000"),
                    StylishConnectedCardItem("経費", "¥320,000"),
                ),
            )
            StylishConnectedCardColumn(
                items = listOf(
                    StylishConnectedCardItem(title = "テーマ", supportingText = "システム設定を使用"),
                    StylishConnectedCardItem(title = "通知", supportingText = "オン"),
                ),
            )
            StylishNavigationBar(
                items = listOf(
                    StylishNavigationItem(Icons.Default.Home, "ホーム", selected = true),
                    StylishNavigationItem(Icons.Default.Search, "検索"),
                    StylishNavigationItem(Icons.Default.Settings, "設定"),
                ),
            )
            SimplePieChart(
                data = listOf(
                    PieChartData("食品", 40f, stylishChartColor(0)),
                    PieChartData("交通", 30f, stylishChartColor(1)),
                    PieChartData("住居", 30f, stylishChartColor(2)),
                ),
                contentDescriptionPrefix = "支出の内訳",
                chartSize = 96.dp,
                animate = false,
            )
            SimpleBarChart(
                data = listOf(
                    BarChartData("1月", 60f),
                    BarChartData("2月", 90f),
                    BarChartData("3月", 40f),
                ),
                contentDescriptionPrefix = "月別売上",
                emptyLabel = "データなし",
                chartHeight = 96.dp,
            )
            SimpleLineChart(
                data = listOf(
                    LineChartData("月", 10f),
                    LineChartData("火", 25f),
                    LineChartData("水", 18f),
                    LineChartData("木", 32f),
                ),
                contentDescriptionPrefix = "週間推移",
                emptyLabel = "データなし",
                chartHeight = 96.dp,
            )
        }
    }
}

/** Fixed-size matrix scene; no animations or wall-clock values are allowed. */
@Composable
private fun MatrixScene(scenario: VisualScenario) {
    val longText = "非常に長いラベルでも折り返しと余白が崩れないことを確認するテキスト"
    Surface(
        modifier = Modifier.width(scenario.widthDp.dp),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = when (scenario.state) {
                    VisualState.LongText -> longText
                    VisualState.Error -> "エラー：読み込みに失敗しました"
                    VisualState.Empty -> "表示するデータがありません"
                    VisualState.Loading -> "読み込み中"
                    VisualState.Disabled -> "無効な操作"
                    VisualState.Default -> "Stylish UI"
                },
                color = if (scenario.state == VisualState.Error) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
            )
            when (scenario.state) {
                VisualState.Loading -> CircularProgressIndicator()
                VisualState.Empty -> Text("空の状態を明示し、次の操作を案内します")
                else -> {
                    StylishButton(
                        onClick = {},
                        enabled = scenario.state != VisualState.Disabled,
                        modifier = Modifier.width((scenario.widthDp - 32).dp),
                    ) {
                        Text(if (scenario.state == VisualState.LongText) longText else "続ける")
                    }
                    StylishChip(
                        label = if (scenario.state == VisualState.LongText) longText else "フィルタ",
                        onClick = {},
                        enabled = scenario.state != VisualState.Disabled,
                    )
                }
            }
            StylishCard(
                title = if (scenario.state == VisualState.LongText) longText else "カードタイトル",
                supportingText = when (scenario.state) {
                    VisualState.Error -> "再試行してください"
                    VisualState.Empty -> "条件を変更して検索できます"
                    VisualState.Loading -> "データを準備しています"
                    else -> "補足テキスト"
                },
            )
        }
    }
}
