package com.segnities007.stylishui.migrationconsumer

import com.segnities007.stylishui.structure.StylishGridSlot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class MigrationConsumerTest {
    @Test
    fun spansBothExtractedArtifacts() {
        val plan = MigrationConsumer.gridPlan(
            tiles = listOf(
                MigrationTile("a", "Alpha"),
                MigrationTile("b", "Beta"),
                MigrationTile("c", "Gamma"),
            ),
            columns = 2,
        )

        // Structure: two rows, the last padded with explicit empty cells.
        assertEquals(2, plan.size)
        assertEquals(2, plan[0].size)
        // A local binding is required so the assertIs contract can smart-cast;
        // indexed access expressions are never smart-cast targets.
        val firstCell = plan[0][0]
        assertIs<StylishGridSlot.Item<MigrationTile>>(firstCell)
        assertEquals("a", firstCell.value.id)
        assertEquals(0, firstCell.index)
        assertIs<StylishGridSlot.Empty>(plan[1][1])

        // Foundation: pure reducer and viewport normalization.
        assertTrue(MigrationConsumer.selectionReducer.reduce(false, Unit))
        assertEquals(0f, MigrationConsumer.normalizedViewport(-5f, 100f).widthPx)
    }
}
