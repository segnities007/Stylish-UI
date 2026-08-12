package com.segnities007.stylishui.visual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
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
import com.segnities007.stylishui.components.models.StylishConnectedListItem
import com.segnities007.stylishui.components.models.StylishNavigationItem
import com.segnities007.stylishui.components.molecules.StylishConnectedCardRow
import com.segnities007.stylishui.components.molecules.StylishConnectedListItemColumn
import com.segnities007.stylishui.components.organisms.StylishNavigationBar
import com.segnities007.stylishui.theme.StylishTheme
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.test.Test
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

    @Test
    fun lightThemeGolden() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = false) {
                GoldenScene()
            }
        }
        assertGolden("light")
    }

    @Test
    fun darkThemeGolden() = runComposeUiTest {
        setContent {
            StylishTheme(darkTheme = true) {
                GoldenScene()
            }
        }
        assertGolden("dark")
    }

    private fun ComposeUiTest.assertGolden(name: String) {
        onRoot().assertExists()
        waitForIdle()
        val current = onRoot().captureToImage().toPixelMap()
        val file = baselineFile(name)
        if (!file.exists()) {
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
        File("src/jvmTest/resources/golden", "$name-golden.png")

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
            StylishConnectedListItemColumn(
                items = listOf(
                    StylishConnectedListItem(headline = "テーマ", supportingText = "システム設定を使用"),
                    StylishConnectedListItem(headline = "通知", supportingText = "オン"),
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
