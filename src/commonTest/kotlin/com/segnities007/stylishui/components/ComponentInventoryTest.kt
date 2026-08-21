package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.molecules.StylishButtonGroupOrientation
import kotlin.test.Test
import kotlin.test.assertEquals

/** Contract tests for the cross-platform action-container inventory. */
class ComponentInventoryTest {
    @Test
    fun buttonGroupPublishesStableResponsiveOrientations() {
        assertEquals(
            listOf(
                StylishButtonGroupOrientation.Horizontal,
                StylishButtonGroupOrientation.Vertical,
            ),
            StylishButtonGroupOrientation.entries.toList(),
        )
    }
}
