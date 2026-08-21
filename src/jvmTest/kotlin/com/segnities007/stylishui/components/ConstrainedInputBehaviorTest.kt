package com.segnities007.stylishui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.v2.runComposeUiTest
import com.segnities007.stylishui.components.atoms.StylishNumberInput
import com.segnities007.stylishui.components.atoms.StylishPinInput
import com.segnities007.stylishui.theme.StylishTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(androidx.compose.ui.test.ExperimentalTestApi::class)
class ConstrainedInputBehaviorTest {

    @Test
    fun numberInputClampsTypedValuesAndRejectsInvalidText() = runComposeUiTest {
        var value by mutableIntStateOf(4)
        setContent {
            StylishTheme(darkTheme = false) {
                StylishNumberInput(
                    value = value,
                    onValueChange = { value = it },
                    range = 0..10,
                )
            }
        }

        val input = onNode(hasSetTextAction())
        input.performTextReplacement("99")
        assertEquals(10, value)

        input.performTextReplacement("not-a-number")
        assertEquals(10, value)

        input.performTextReplacement("-2")
        assertEquals(0, value)
    }

    @Test
    fun numberInputClampsStepsAndDisablesControlsAtBounds() = runComposeUiTest {
        var value by mutableIntStateOf(8)
        setContent {
            StylishTheme(darkTheme = false) {
                StylishNumberInput(
                    value = value,
                    onValueChange = { value = it },
                    range = 0..10,
                    step = 5,
                    decrementContentDescription = "減少",
                    incrementContentDescription = "増加",
                )
            }
        }

        val increment = onNodeWithContentDescription("増加")
        val decrement = onNodeWithContentDescription("減少")

        increment.assertIsEnabled().performClick()
        assertEquals(10, value)
        increment.assertIsNotEnabled()

        decrement.performClick()
        assertEquals(5, value)
        decrement.performClick()
        assertEquals(0, value)
        decrement.assertIsNotEnabled()
    }

    @Test
    fun pinInputFiltersPastedTextAndCapsItAtConfiguredLength() = runComposeUiTest {
        var value by mutableStateOf("")
        setContent {
            StylishTheme(darkTheme = false) {
                StylishPinInput(
                    value = value,
                    onValueChange = { value = it },
                    length = 4,
                )
            }
        }

        val input = onNode(hasSetTextAction())
        input.performTextReplacement("1a2-3456")
        assertEquals("1234", value)

        input.performTextReplacement("9x8")
        assertEquals("98", value)
    }
}
