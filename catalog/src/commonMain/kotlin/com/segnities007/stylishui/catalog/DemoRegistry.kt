package com.segnities007.stylishui.catalog

/**
 * Central registry that collects all demo components from each category.
 *
 * Provides a single entry point for the playground to access all available demos,
 * enabling filtering, searching, and grid-based display.
 */
internal object DemoRegistry {
    /**
     * Returns all available demo components across all categories.
     */
    val allDemos: List<DemoComponent> by lazy {
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
            getExtendedCoverageDemos()
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
