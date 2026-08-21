package com.segnities007.stylishui.foundation

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/**
 * Stable identifier namespace used by Stylish UI's component roots.
 *
 * Component tags are deliberately separate from user supplied test tags. A
 * host application may still append its own [Modifier.testTag] after a
 * Stylish component call, while integration tests can rely on a stable
 * identifier that is independent of the rendered copy or theme.
 */
public object StylishAccessibilityTags {
    /** Prefix reserved for the built-in component identifiers. */
    public const val Prefix: String = "stylish_"

    /**
     * Returns the canonical value for a static component identifier.
     *
     * The input is intentionally constrained to a small, portable alphabet so
     * the same value can be consumed by Compose UI tests, Android UIAutomator,
     * and browser accessibility/e2e adapters.
     */
    public fun value(component: String): String {
        require(component.matches(ComponentNamePattern)) {
            "component must contain only lowercase letters, digits, '_' or '-': $component"
        }
        return if (component.startsWith(Prefix)) component else "$Prefix$component"
    }

    private val ComponentNamePattern = Regex("[a-z0-9][a-z0-9_-]*")
}

/**
 * Adds Stylish UI's stable component identifier to a root node.
 *
 * Use a static component name (for example `"button"` or
 * `"navigation_rail"`) rather than user-facing text. Dynamic collection
 * items should append their own stable key to the name at the call site.
 */
public fun Modifier.stylishTestTag(component: String): Modifier =
    testTag(StylishAccessibilityTags.value(component))
