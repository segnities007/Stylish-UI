package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.models.StylishContentState
import com.segnities007.stylishui.components.models.mapContent
import kotlin.test.Test
import kotlin.test.assertEquals

class StylishContentStateTest {
    @Test
    fun mapContentOnlyTransformsContent() {
        assertEquals(
            StylishContentState.Content(42),
            StylishContentState.Content("42").mapContent(String::toInt),
        )
        assertEquals(
            StylishContentState.Loading,
            StylishContentState.Loading.mapContent { it.toString() },
        )
        assertEquals(
            StylishContentState.Empty("No results"),
            StylishContentState.Empty("No results").mapContent { it.toString() },
        )
    }
}
