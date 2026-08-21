package com.segnities007.stylishui.visual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.segnities007.stylishui.components.atoms.StylishButton
import com.segnities007.stylishui.components.atoms.StylishFilledTextField
import com.segnities007.stylishui.components.atoms.StylishCard
import com.segnities007.stylishui.components.charts.SimpleLineChart
import com.segnities007.stylishui.components.molecules.StylishEmptyState
import com.segnities007.stylishui.theme.StylishJapaneseStrings
import com.segnities007.stylishui.theme.StylishTheme
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Deterministic Linux visual-resilience matrix.
 *
 * This is deliberately separate from the two fixed 393dp light/dark goldens. It exercises the
 * combinations that most often expose designer-facing regressions: narrow width, RTL, 200%
 * font scale, high contrast, long copy, and the default/loading/error/empty/disabled states. The test does not
 * claim native Dynamic Type, TalkBack, VoiceOver, or browser rendering parity. Set
 * `WRITE_VISUAL_MATRIX=1` when a local/CI job wants PNG artifacts under `build/reports/`.
 */
@OptIn(ExperimentalTestApi::class)
class VisualRegressionMatrixTest {

    @Test
    fun linuxVisualMatrixKeepsStatesVisibleAndCanvasDeterministic() {
        matrixCases().forEach { case ->
            runComposeUiTest {
                setContent { VisualMatrixScene(case) }
                onNodeWithText("エラー状態").assertExists()
                onNodeWithText("空状態").assertExists()
                onNodeWithText("無効状態").assertExists()
                waitForIdle()

                val image = onNodeWithTag("visual_matrix_root").captureToImage().toPixelMap()
                assertTrue(image.width >= case.width, "width collapsed for ${case.id}")
                assertTrue(image.height > 0, "height collapsed for ${case.id}")
                assertVisualContent(image, case)
                writeArtifactIfRequested(image, case)
            }
        }
    }

    private fun matrixCases(): List<MatrixCase> = buildList {
        for (darkTheme in listOf(false, true)) {
            for (highContrast in listOf(false, true)) {
                for (rtl in listOf(false, true)) {
                    for (fontScale in listOf(1f, 2f)) {
                    add(
                        MatrixCase(
                            id = buildString {
                                append(if (darkTheme) "dark" else "light")
                                if (highContrast) append("-hc")
                                append(if (rtl) "-rtl" else "-ltr")
                                append(if (fontScale == 2f) "-200" else "-100")
                            },
                            darkTheme = darkTheme,
                            highContrast = highContrast,
                            rtl = rtl,
                            fontScale = fontScale,
                            width = 320,
                            height = 1200,
                        ),
                    )
                    }
                }
            }
        }
    }

    private fun assertVisualContent(image: PixelMap, case: MatrixCase) {
        val background = image[0, 0].toArgb()
        var nonBackground = 0
        val buckets = HashSet<Int>()
        for (y in 0 until image.height step 4) {
            for (x in 0 until image.width step 4) {
                val argb = image[x, y].toArgb()
                if (argb != background) nonBackground++
                buckets += (argb and 0x00FFFFFF) shr 4
            }
        }
        val sampledPixels = (image.width / 4) * (image.height / 4)
        assertTrue(
            nonBackground > sampledPixels / 100,
            "${case.id} rendered as an almost empty surface",
        )
        assertTrue(
            buckets.size >= 4,
            "${case.id} lost visual state/color diversity",
        )
    }

    private fun writeArtifactIfRequested(image: PixelMap, case: MatrixCase) {
        if (System.getenv("WRITE_VISUAL_MATRIX") != "1") return
        val directory = File("build/reports/visual-matrix")
        directory.mkdirs()
        val output = File(directory, "${case.id}.png")
        val buffered = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                buffered.setRGB(x, y, image[x, y].toArgb())
            }
        }
        check(ImageIO.write(buffered, "png", output)) {
            "Unable to write visual matrix artifact: ${output.absolutePath}"
        }
    }

    @Composable
    private fun VisualMatrixScene(case: MatrixCase) {
        CompositionLocalProvider(
            LocalLayoutDirection provides if (case.rtl) LayoutDirection.Rtl else LayoutDirection.Ltr,
            LocalDensity provides Density(1f, case.fontScale),
        ) {
            StylishTheme(
                darkTheme = case.darkTheme,
                highContrast = case.highContrast,
                strings = StylishJapaneseStrings,
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(case.width.dp)
                        .requiredHeight(case.height.dp)
                        .testTag("visual_matrix_root"),
                ) {
                    Surface(Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                "状態マトリクス：非常に長い見出しでも視覚階層と折り返しを維持する確認",
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            StylishButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                                Text("通常状態")
                            }
                            StylishButton(
                                onClick = {},
                                isLoading = true,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("読み込み状態")
                            }
                            StylishButton(
                                onClick = {},
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("無効状態")
                            }
                            StylishFilledTextField(
                                value = "",
                                onValueChange = {},
                                label = "エラー状態：非常に長い入力ラベルを折り返す",
                                placeholder = "長いプレースホルダーでも入力領域を壊さない",
                                isError = true,
                                errorMessage = "エラー状態：入力内容を確認してください。詳細な説明が複数行になっても読めること。",
                                modifier = Modifier.fillMaxWidth(),
                            )
                            StylishCard(
                                title = "長文カード：日本語とEnglish mixed content should wrap without clipping",
                                supportingText = "補足説明が長い場合も、カードの視覚的な階層と余白を保ったまま次の要素へ流れることを確認します。",
                                modifier = Modifier.fillMaxWidth(),
                            )
                            SimpleLineChart(
                                data = emptyList(),
                                contentDescriptionPrefix = "空チャート",
                                emptyLabel = "データがありません",
                                animate = false,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            StylishEmptyState(
                                icon = Icons.Default.Search,
                                title = "空状態",
                                description = "表示するデータがありません。条件を変更して再試行してください。",
                                modifier = Modifier.fillMaxWidth(),
                            )
                            StylishEmptyState(
                                icon = Icons.Default.ErrorOutline,
                                title = "エラー状態",
                                description = "データ取得に失敗しました。時間を置いて再試行してください。",
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }

    private data class MatrixCase(
        val id: String,
        val darkTheme: Boolean,
        val highContrast: Boolean,
        val rtl: Boolean,
        val fontScale: Float,
        val width: Int,
        val height: Int,
    )
}
