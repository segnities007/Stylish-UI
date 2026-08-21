package com.segnities007.stylishui.structure

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StylishSlotLayoutsTest {
    @Test
    fun gridPlanPreservesOrderAndPadsFinalRow() {
        val rows = stylishGridRows(listOf("a", "b", "c", "d", "e"), columns = 3)

        assertEquals(2, rows.size)
        assertEquals(
            listOf(
                StylishGridSlot.Item("a", 0),
                StylishGridSlot.Item("b", 1),
                StylishGridSlot.Item("c", 2),
            ),
            rows[0],
        )
        assertEquals(
            listOf(
                StylishGridSlot.Item("d", 3),
                StylishGridSlot.Item("e", 4),
                StylishGridSlot.Empty,
            ),
            rows[1],
        )
    }

    @Test
    fun gridPlanSupportsNullableDomainValues() {
        val rows = stylishGridRows(listOf(null, "present"), columns = 2)

        assertEquals(StylishGridSlot.Item(null, 0), rows.single()[0])
        assertEquals(StylishGridSlot.Item("present", 1), rows.single()[1])
    }

    @Test
    fun emptyInputProducesNoRows() {
        assertEquals(emptyList(), stylishGridRows(emptyList<String>(), columns = 2))
    }

    @Test
    fun nonPositiveColumnsAreRejected() {
        assertFailsWith<IllegalArgumentException> {
            stylishGridRows(listOf("item"), columns = 0)
        }
    }
}
