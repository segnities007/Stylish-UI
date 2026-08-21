package com.segnities007.stylishui.structureconsumer

import com.segnities007.stylishui.structure.stylishGridRows

/**
 * Tiny downstream-facing adapter used by the module-boundary build gate.
 *
 * It intentionally imports only the physical `:structure` artifact. A host
 * can use this plan to render with Compose, SwiftUI, UIKit, DOM, or a custom
 * renderer without importing Stylish Finish components.
 */
public fun structureConsumerRowCount(items: List<String>, columns: Int): Int =
    stylishGridRows(items, columns).size
