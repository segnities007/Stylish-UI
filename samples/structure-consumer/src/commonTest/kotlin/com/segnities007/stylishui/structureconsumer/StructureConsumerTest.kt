package com.segnities007.stylishui.structureconsumer

import kotlin.test.Test
import kotlin.test.assertEquals

class StructureConsumerTest {
    @Test
    fun consumesPhysicalStructureArtifact() {
        assertEquals(2, structureConsumerRowCount(listOf("a", "b", "c"), columns = 2))
    }
}
