package com.segnities007.stylishui.catalog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DemoRegistryTest {
    @Test
    fun catalogDoesNotExposeStandaloneDuplicates() {
        val names = DemoRegistry.allDemos.map { it.name }
        val duplicateStandaloneNames = setOf(
            "Tooltip",
            "Drag handle",
            "TriState checkbox",
            "Radio group",
            "Connected toggle row",
            "Outlined text field",
            "Autocomplete",
            "Large top app bar",
            "Gradient footer",
            "Gradient navigation rail",
            "Snackbar",
            "Popconfirm",
            "Delete confirm dialog",
            "Empty state",
            "Connected card row",
            "Connected card grid",
            "Header",
            "Footer",
            "Page content",
            "Elevation levels",
        )

        assertTrue(names.intersect(duplicateStandaloneNames).isEmpty())
        assertEquals(names.size, names.distinct().size)
    }

    @Test
    fun relatedUiVariantsAreRepresentedByOneCatalogEntry() {
        val demos = DemoRegistry.allDemos.associateBy { it.name }

        assertTrue("Text fields" in demos)
        assertTrue("StylishOutlinedTextField" in demos.getValue("Text fields").code)
        assertTrue("StylishAutocomplete" in demos.getValue("Text fields").code)
        assertTrue("StylishTooltip" in demos.getValue("Icon buttons").code)
        assertTrue("StylishDragHandle" in demos.getValue("Bottom sheet").code)
        assertFalse("Connected toggle row" in demos)
    }
}
