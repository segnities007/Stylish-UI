package com.segnities007.stylishui.theme

import kotlin.test.Test
import kotlin.test.assertEquals

class StylishStringsTest {
    @Test
    fun japaneseDateAndPluralAreLocaleAware() {
        assertEquals("2026年8月20日", StylishJapaneseStrings.formatDate(2026, 8, 20))
        assertEquals("3件", StylishJapaneseStrings.plural(3, "件"))
    }

    @Test
    fun advancedLabelsAreLocaleOwned() {
        assertEquals("Move Name left", StylishStrings().moveColumnLeft("Name"))
        assertEquals("Nameを左へ移動", StylishJapaneseStrings.moveColumnLeft("Name"))
        assertEquals("12バイト", StylishJapaneseStrings.fileSizeBytes(12))
    }
}
