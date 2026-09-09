package com.segnities007.stylishui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.segnities007.stylishui.tokens.StylishAnimationTokens
import com.segnities007.stylishui.tokens.StylishDimensions
import com.segnities007.stylishui.tokens.StylishShapes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class StylishThemeBrandTest {

    @Test
    fun defaultSchemesKeepWarmLayerContrast() {
        val light = StylishLightColorScheme
        val dark = StylishDarkColorScheme

        assertTrue(light.background.luminance() > dark.background.luminance())
        assertTrue(light.surface.luminance() > light.background.luminance())
        assertTrue(dark.surfaceBright.luminance() > dark.background.luminance())
        assertTrue(light.onBackground.luminance() < light.background.luminance())
        assertTrue(dark.onBackground.luminance() > dark.background.luminance())

        // A dark milk-tea endpoint is warm but restrained: it must not have
        // the strong red cast of the previous red-brown endpoint.
        assertTrue(
            dark.surfaceBright.red - dark.surfaceBright.blue < 0.15f,
            "dark surfaceBright is too red: ${dark.surfaceBright}",
        )
    }

    @Test
    fun lightBrandParametersApplyMaterialAndStylishTokens() = runComposeUiTest {
        val brandShape = CutCornerShape(9.dp)
        val componentColors = componentColors(button = Color(0xFF2468AC))
        var captured: CapturedTheme? = null

        setContent {
            TestBrandTheme(
                darkTheme = false,
                brandShape = brandShape,
                lightComponentColors = componentColors,
            ) {
                captured = captureTheme()
            }
        }

        runOnIdle {
            assertEquals(Color(0xFF123456), captured?.primary)
            assertEquals(19.sp, captured?.labelSize)
            assertEquals(brandShape, captured?.materialShape)
            assertEquals(brandShape, captured?.stylishShape)
            assertEquals(23.dp, captured?.connectedCornerRadius)
            assertEquals(61.dp, captured?.sectionSpacing)
            assertEquals(321, captured?.durationShort)
            assertEquals(componentColors.button, captured?.buttonColor)
        }
    }

    @Test
    fun darkBrandParametersSelectDarkColorAndDerivedComponentColor() = runComposeUiTest {
        var primary: Color? = null
        var button: Color? = null

        setContent {
            TestBrandTheme(darkTheme = true) {
                primary = MaterialTheme.colorScheme.primary
                button = MaterialTheme.stylishComponentColors.button
            }
        }

        runOnIdle {
            assertEquals(Color(0xFFABCDEF), primary)
            assertEquals(Color(0xFFABCDEF), button)
        }
    }

    @Test
    fun highContrastBrandParametersRecomputeComponentColors() = runComposeUiTest {
        val highContrastPrimary = Color(0xFF6600CC)
        var primary: Color? = null
        var button: Color? = null

        setContent {
            TestBrandTheme(
                darkTheme = false,
                highContrast = true,
                highContrastLightColorScheme = StylishHighContrastLightColorScheme.copy(
                    primary = highContrastPrimary,
                ),
                lightComponentColors = componentColors(button = Color.Red),
            ) {
                primary = MaterialTheme.colorScheme.primary
                button = MaterialTheme.stylishComponentColors.button
            }
        }

        runOnIdle {
            assertEquals(highContrastPrimary, primary)
            assertEquals(highContrastPrimary, button)
        }
    }
}

private data class CapturedTheme(
    val primary: Color,
    val labelSize: TextUnit,
    val materialShape: Shape,
    val stylishShape: Shape,
    val connectedCornerRadius: Dp,
    val sectionSpacing: Dp,
    val durationShort: Int,
    val buttonColor: Color,
)

@Composable
private fun captureTheme(): CapturedTheme = CapturedTheme(
    primary = MaterialTheme.colorScheme.primary,
    labelSize = MaterialTheme.typography.labelLarge.fontSize,
    materialShape = MaterialTheme.shapes.small,
    stylishShape = StylishTheme.shapes.medium,
    connectedCornerRadius = StylishTheme.shapes.connectedCornerRadius,
    sectionSpacing = StylishTheme.dimensions.sectionSpacing,
    durationShort = StylishTheme.animation.durationShort,
    buttonColor = MaterialTheme.stylishComponentColors.button,
)

@Composable
private fun TestBrandTheme(
    darkTheme: Boolean,
    brandShape: CornerBasedShape = CutCornerShape(7.dp),
    highContrast: Boolean = false,
    highContrastLightColorScheme: androidx.compose.material3.ColorScheme? = null,
    lightComponentColors: StylishComponentColors? = null,
    content: @Composable () -> Unit,
) {
    StylishTheme(
        lightColorScheme = StylishLightColorScheme.copy(primary = Color(0xFF123456)),
        darkColorScheme = StylishDarkColorScheme.copy(primary = Color(0xFFABCDEF)),
        darkTheme = darkTheme,
        highContrast = highContrast,
        highContrastLightColorScheme = highContrastLightColorScheme,
        typography = Typography(labelLarge = TextStyle(fontSize = 19.sp)),
        dimensions = StylishDimensions(sectionSpacing = 61.dp),
        shapes = StylishShapes(
            small = brandShape,
            medium = brandShape,
            connectedCornerRadius = 23.dp,
        ),
        animation = StylishAnimationTokens(durationShort = 321),
        lightComponentColors = lightComponentColors,
        content = content,
    )
}

private fun componentColors(button: Color): StylishComponentColors = StylishComponentColors(
    groupedContainer = Color.White,
    card = Color.White,
    cardContent = Color.Black,
    button = button,
    buttonContent = Color.White,
    chip = Color.White,
    chipContent = Color.Black,
    textField = Color.White,
    textFieldContent = Color.Black,
)
