package com.segnities007.stylishui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Verifies that composed surfaces are normalized against their live root range. */
@OptIn(ExperimentalTestApi::class)
class StylishLayerDepthIntegrationTest {
    @Test
    fun surfaceRolesAdvanceWithinTheSameStructuralScope() = runComposeUiTest {
        var cardColor = Color.Unspecified
        var controlColor = Color.Unspecified
        var floatingColor = Color.Unspecified
        var nestedCardColor = Color.Unspecified

        setContent {
            StylishTheme(darkTheme = false) {
                StylishElevationLayer {
                    cardColor = stylishCardContainerColor()
                    controlColor = stylishElevatedControlContainerColor()
                    floatingColor = stylishFloatingContainerColor(alpha = 1f)
                    StylishElevationLayer {
                        nestedCardColor = stylishCardContainerColor()
                    }
                }
            }
        }

        waitForIdle()

        assertTrue(
            controlColor.luminance() > cardColor.luminance(),
            "an elevated control must be brighter than a card at the same structural depth " +
                "(${controlColor.luminance()} <= ${cardColor.luminance()})",
        )
        assertTrue(
            floatingColor.luminance() > controlColor.luminance(),
            "a floating surface must be brighter than an elevated control at the same structural depth " +
                "(${floatingColor.luminance()} <= ${controlColor.luminance()})",
        )
        assertTrue(
            nestedCardColor.luminance() > floatingColor.luminance(),
            "a nested card must remain brighter than its floating parent " +
                "(${nestedCardColor.luminance()} <= ${floatingColor.luminance()})",
        )
    }

    @Test
    fun surfaceRolesAdvanceWithinTheSameStructuralScopeInDarkTheme() = runComposeUiTest {
        var cardColor = Color.Unspecified
        var controlColor = Color.Unspecified
        var floatingColor = Color.Unspecified

        setContent {
            StylishTheme(darkTheme = true) {
                StylishElevationLayer {
                    cardColor = stylishCardContainerColor()
                    controlColor = stylishElevatedControlContainerColor()
                    floatingColor = stylishFloatingContainerColor(alpha = 1f)
                }
            }
        }

        waitForIdle()

        assertTrue(
            controlColor.luminance() > cardColor.luminance(),
            "dark elevated control must be brighter than a dark card " +
                "(${controlColor.luminance()} <= ${cardColor.luminance()})",
        )
        assertTrue(
            floatingColor.luminance() > controlColor.luminance(),
            "dark floating surface must be brighter than a dark elevated control " +
                "(${floatingColor.luminance()} <= ${controlColor.luminance()})",
        )
    }

    @Test
    fun nestedSurfacesUseTheObservedEndpoints() = runComposeUiTest {
        var outerColor = Color.Unspecified
        var innerColor = Color.Unspecified
        var background = Color.Unspecified
        var surfaceBright = Color.Unspecified

        setContent {
            StylishTheme(darkTheme = false) {
                background = MaterialTheme.colorScheme.background
                surfaceBright = MaterialTheme.colorScheme.surfaceBright
                StylishElevationLayer {
                    outerColor = stylishCardContainerColor()
                    StylishElevationLayer {
                        innerColor = stylishCardContainerColor()
                    }
                }
            }
        }

        waitForIdle()

        assertEquals(background, outerColor)
        assertEquals(surfaceBright, innerColor)
        assertTrue(innerColor.luminance() > outerColor.luminance())
    }

    @Test
    fun nestedSurfacesUseObservedEndpointsInDarkTheme() = runComposeUiTest {
        var outerColor = Color.Unspecified
        var innerColor = Color.Unspecified
        var background = Color.Unspecified
        var surfaceBright = Color.Unspecified

        setContent {
            StylishTheme(darkTheme = true) {
                background = MaterialTheme.colorScheme.background
                surfaceBright = MaterialTheme.colorScheme.surfaceBright
                StylishElevationLayer {
                    outerColor = stylishCardContainerColor()
                    StylishElevationLayer {
                        innerColor = stylishCardContainerColor()
                    }
                }
            }
        }

        waitForIdle()

        assertEquals(background, outerColor)
        assertEquals(surfaceBright, innerColor)
        assertTrue(innerColor.luminance() > outerColor.luminance())
    }

    @Test
    fun removingTheDeepestSurfaceRecomputesTheLiveRange() = runComposeUiTest {
        val showInner = mutableStateOf(true)
        var outerColor = Color.Unspecified
        var background = Color.Unspecified

        setContent {
            StylishTheme(darkTheme = false) {
                background = MaterialTheme.colorScheme.background
                StylishElevationLayer {
                    outerColor = stylishCardContainerColor()
                    if (showInner.value) {
                        StylishElevationLayer {
                            stylishCardContainerColor()
                        }
                    }
                }
            }
        }

        waitForIdle()
        assertEquals(background, outerColor)

        showInner.value = false
        waitForIdle()

        assertTrue(outerColor.luminance() > background.luminance())
    }
}
