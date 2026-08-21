package com.segnities007.stylishui.components

import com.segnities007.stylishui.components.organisms.StylishDataTableQuery
import com.segnities007.stylishui.components.organisms.StylishDataTableQueryResult
import com.segnities007.stylishui.components.organisms.StylishDataTableAdapter
import com.segnities007.stylishui.components.organisms.loadNormalized
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.test.Test
import kotlin.test.assertEquals

class DataTableQueryTest {
    @Test
    fun queryNormalizesInvalidPagingWithoutChangingHoistedValues() {
        val query = StylishDataTableQuery(page = 0, pageSize = -10, filter = "open")

        assertEquals(1, query.normalizedPage)
        assertEquals(1, query.normalizedPageSize)
        assertEquals("open", query.filter)
    }

    @Test
    fun resultCarriesRowsAndOptionalServerMetadata() {
        val result = StylishDataTableQueryResult(rows = listOf("a", "b"), totalRowCount = 20, hasNextPage = true)

        assertEquals(listOf("a", "b"), result.rows)
        assertEquals(20, result.totalRowCount)
        assertEquals(true, result.hasNextPage)
    }

    @Test
    fun adapterReceivesNormalizedPagingValues() {
        var received: StylishDataTableQuery? = null
        val adapter = StylishDataTableAdapter<String> { query ->
            received = query
            StylishDataTableQueryResult(rows = listOf("row"))
        }

        runSuspend {
            adapter.loadNormalized(StylishDataTableQuery(page = 0, pageSize = 0))
        }

        assertEquals(1, received?.page)
        assertEquals(1, received?.pageSize)
    }

    private fun <T> runSuspend(block: suspend () -> T): T {
        var outcome: Result<T>? = null
        block.startCoroutine(object : Continuation<T> {
            override val context = EmptyCoroutineContext

            override fun resumeWith(result: Result<T>) {
                outcome = result
            }
        })
        return requireNotNull(outcome).getOrThrow()
    }
}
