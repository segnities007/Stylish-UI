package com.segnities007.stylishui.catalog

/**
 * Central registry that collects all demo components from each category.
 *
 * Provides a single entry point for the playground to access all available demos,
 * enabling filtering, searching, and grid-based display.
 */
internal object DemoRegistry {
    private val demosRepresentedByAnotherCatalogEntry = setOf(
        "Divider",
        "Dot indicator",
        "Tab bar",
        "Form field",
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

    /**
     * Returns all available demo components across all categories.
     */
    val allDemos: List<DemoComponent> by lazy {
        (
            getButtonDemos() +
                getSelectionDemos() +
                getInputDemos() +
                getNavigationDemos() +
                getFeedbackDemos() +
                getConnectedDemos() +
                getChartDemos() +
                getAdvancedDemos() +
                getWebParityDemos() +
                getPatternDemos() +
                getCoverageDemos() +
                getExtendedCoverageDemos() +
                getDesignHarnessDemos()
        ).filterNot { it.name in demosRepresentedByAnotherCatalogEntry }
    }

    /**
     * Returns demos filtered by category.
     *
     * @param category The category to filter by, or null for all demos.
     */
    fun getDemosByCategory(category: DemoCategory?): List<DemoComponent> {
        return if (category == null) {
            allDemos
        } else {
            allDemos.filter { it.category == category }
        }
    }

    /**
     * Returns the count of demos per category.
     */
    fun getCategoryCounts(): Map<DemoCategory, Int> {
        return DemoCategory.entries.associateWith { category ->
            allDemos.count { it.category == category }
        }
    }
}
