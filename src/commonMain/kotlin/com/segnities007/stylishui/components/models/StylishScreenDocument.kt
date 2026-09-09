package com.segnities007.stylishui.components.models

/**
 * Closed screen vocabulary. No renderer callbacks or arbitrary modifiers are accepted:
 * each element's visual and interaction contract is owned by Stylish UI.
 */
public sealed interface StylishScreenElement {
    /** Stable, nonblank identity, unique within a document. */
    public val id: String

    /** Read-only text with semantic hierarchy. */
    public data class Text(
        override val id: String,
        public val text: String,
        public val role: StylishTextRole = StylishTextRole.Body,
    ) : StylishScreenElement

    /** A navigable or read-only list row. Null [actionId] means non-interactive. */
    public data class Entry(
        override val id: String,
        public val title: String,
        public val supportingText: String? = null,
        public val actionId: String? = null,
        public val enabled: Boolean = true,
    ) : StylishScreenElement

    /** A labeled action; [loading] blocks activation as well as rendering progress. */
    public data class Action(
        override val id: String,
        public val label: String,
        public val enabled: Boolean = true,
        public val loading: Boolean = false,
    ) : StylishScreenElement

    /** Controlled text input. Validation messages are supplied by the host. */
    public data class Input(
        override val id: String,
        public val label: String,
        public val value: String,
        public val error: String? = null,
        public val enabled: Boolean = true,
    ) : StylishScreenElement

    /** Controlled, labeled switch with a single accessible interaction target. */
    public data class Toggle(
        override val id: String,
        public val label: String,
        public val checked: Boolean,
        public val enabled: Boolean = true,
    ) : StylishScreenElement
}

/** Host events; navigation, persistence and domain validation stay in the application. */
public sealed interface StylishScreenEvent {
    /** Button or entry activation. */
    public data class Activate(public val id: String) : StylishScreenEvent
    /** Text edit emitted by a controlled field. */
    public data class Edit(public val id: String, public val value: String) : StylishScreenEvent
    /** Switch change emitted by a controlled field. */
    public data class Toggle(public val id: String, public val checked: Boolean) : StylishScreenEvent
    /** Retry requested from the error presentation. */
    public data object Retry : StylishScreenEvent
}

/**
 * Validated snapshot of a screen. Duplicate identities and unlabeled controls are rejected
 * at construction, before LazyColumn or accessibility encounters an ambiguous element.
 */
public class StylishScreenDocument(elements: List<StylishScreenElement>) {
    /** Defensive snapshot: subsequent mutations to the caller's list do not change the screen. */
    public val elements: List<StylishScreenElement> = elements.toList()

    init {
        require(this.elements.map { it.id }.distinct().size == this.elements.size) {
            "Screen element ids must be unique"
        }
        this.elements.forEach(::validateScreenElement)
    }
}

private fun validateScreenElement(element: StylishScreenElement) {
    require(element.id.isNotBlank()) { "Screen element id must not be blank" }
    val label = when (element) {
        is StylishScreenElement.Text -> element.text
        is StylishScreenElement.Entry -> element.title
        is StylishScreenElement.Action -> element.label
        is StylishScreenElement.Input -> element.label
        is StylishScreenElement.Toggle -> element.label
    }
    require(label.isNotBlank()) { "Screen element '${element.id}' requires visible text" }
    if (element is StylishScreenElement.Entry) {
        require(element.actionId == null || element.actionId.isNotBlank()) {
            "Entry action id must not be blank"
        }
    }
}
