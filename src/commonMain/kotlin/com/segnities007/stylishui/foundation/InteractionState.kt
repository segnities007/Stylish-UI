package com.segnities007.stylishui.foundation

/**
 * Determines whether a component should behave as an interactive (actionable) element.
 *
 * A component is actionable only when it is [enabled] **and** has at least one real action
 * registered ([hasClickAction] or [hasLongClickAction]). Components that are not actionable
 * should render in a display-only style: no ripple, no pointer-icon change, no elevation lift
 * on press.
 *
 * **Contract:** callers must represent a missing action with `null` (e.g. `onClick = null`),
 * never with an empty lambda `{}`. This function exists to centralize that check so that every
 * Connected UI component (cards, list items, chips, buttons) applies the same interactivity
 * logic consistently.
 *
 * @param enabled Whether the component is in an enabled state. Defaults to `true`.
 * @param hasClickAction Whether a non-null click handler was provided.
 * @param hasLongClickAction Whether a non-null long-click handler was provided. Defaults to
 *   `false`.
 * @return `true` if the component should render and respond as interactive.
 * @see com.segnities007.stylishui.components.models.StylishConnectedCardItem
 * @see com.segnities007.stylishui.components.models.StylishConnectedCardItem
 */
public fun isActionable(
    enabled: Boolean = true,
    hasClickAction: Boolean,
    hasLongClickAction: Boolean = false,
): Boolean = enabled && (hasClickAction || hasLongClickAction)
