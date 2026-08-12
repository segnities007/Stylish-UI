package com.segnities007.stylishui.foundation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConnectedGeometryTest {
    @Test
    fun `standalone item exposes every corner`() {
        assertEquals(ConnectedCorners.Standalone, connectedGridCorners(0, 1, 2))
    }

    @Test
    fun `full grid exposes only its four outside corners`() {
        assertEquals(ConnectedCorners(topStart = true), connectedGridCorners(0, 4, 2))
        assertEquals(ConnectedCorners(topEnd = true), connectedGridCorners(1, 4, 2))
        assertEquals(ConnectedCorners(bottomStart = true), connectedGridCorners(2, 4, 2))
        assertEquals(ConnectedCorners(bottomEnd = true), connectedGridCorners(3, 4, 2))
    }

    @Test
    fun `stretched final row joins the junction above it`() {
        // The lone final item stretches to full width, so the item above it
        // keeps only its outer top-end corner; the stretched item exposes its
        // outer bottom corners.
        assertEquals(ConnectedCorners(topEnd = true), connectedGridCorners(1, 3, 2))
        assertEquals(
            ConnectedCorners(bottomStart = true, bottomEnd = true),
            connectedGridCorners(2, 3, 2),
        )
    }

    @Test
    fun `row above a stretched final row has no outer bottom corners`() {
        // n=7, columns=3: the final row holds one stretched item, so every
        // bottom corner of row 1 faces that stretched item and stays joined.
        assertEquals(ConnectedCorners(), connectedGridCorners(3, 7, 3))
        assertEquals(ConnectedCorners(), connectedGridCorners(4, 7, 3))
        assertEquals(ConnectedCorners(), connectedGridCorners(5, 7, 3))
        assertEquals(
            ConnectedCorners(bottomStart = true, bottomEnd = true),
            connectedGridCorners(6, 7, 3),
        )
    }

    @Test
    fun `five items in three columns expose corners adjacent to empty cells`() {
        assertEquals(ConnectedCorners(topStart = true), connectedGridCorners(0, 5, 3))
        assertEquals(ConnectedCorners(), connectedGridCorners(1, 5, 3))
        assertEquals(ConnectedCorners(topEnd = true), connectedGridCorners(2, 5, 3))
        assertEquals(ConnectedCorners(bottomStart = true), connectedGridCorners(3, 5, 3))
        assertEquals(ConnectedCorners(bottomEnd = true), connectedGridCorners(4, 5, 3))
    }

    @Test
    fun `row and column geometry expose only their outer corners`() {
        assertEquals(
            ConnectedCorners(topStart = true, bottomStart = true),
            connectedRowCorners(0, 2),
        )
        assertEquals(
            ConnectedCorners(topEnd = true, bottomEnd = true),
            connectedRowCorners(1, 2),
        )
        assertEquals(
            ConnectedCorners(topStart = true, topEnd = true),
            connectedColumnCorners(0, 2),
        )
        assertEquals(
            ConnectedCorners(bottomStart = true, bottomEnd = true),
            connectedColumnCorners(1, 2),
        )
    }

    @Test
    fun `invalid geometry is rejected at the public boundary`() {
        assertFailsWith<IllegalArgumentException> {
            connectedGridCorners(index = 0, size = 1, columns = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            connectedGridCorners(index = 1, size = 1, columns = 1)
        }
        assertFailsWith<IllegalArgumentException> {
            connectedRowCorners(index = 2, size = 2)
        }
        assertFailsWith<IllegalArgumentException> {
            connectedColumnCorners(index = -1, size = 2)
        }
    }

    @Test
    fun `single column grid behaves like a column`() {
        assertEquals(
            ConnectedCorners(topStart = true, topEnd = true),
            connectedGridCorners(0, 3, 1),
        )
        assertEquals(ConnectedCorners(), connectedGridCorners(1, 3, 1))
        assertEquals(
            ConnectedCorners(bottomStart = true, bottomEnd = true),
            connectedGridCorners(2, 3, 1),
        )
    }

    @Test
    fun `single incomplete row behaves like a row`() {
        assertEquals(
            ConnectedCorners(topStart = true, bottomStart = true),
            connectedGridCorners(0, 2, 3),
        )
        assertEquals(
            ConnectedCorners(topEnd = true, bottomEnd = true),
            connectedGridCorners(1, 2, 3),
        )
    }

    @Test
    fun `connectedShape maps outer corners to the large radius`() {
        assertEquals(
            RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 2.dp,
                bottomStart = 2.dp,
                bottomEnd = 12.dp,
            ),
            connectedShape(
                ConnectedCorners(topStart = true, bottomEnd = true),
                cornerRadius = 12.dp,
                joinedCornerRadius = 2.dp,
            ),
        )
        assertEquals(
            RoundedCornerShape(4.dp),
            connectedShape(
                ConnectedCorners.Standalone,
                cornerRadius = 4.dp,
                joinedCornerRadius = 4.dp,
            ),
        )
    }

    @Test
    fun `edges default to all sides and validate their input`() {
        assertEquals(ConnectedEdges.All, connectedRowEdges(0, 2))
        assertEquals(ConnectedEdges.All, connectedColumnEdges(1, 2))
        assertEquals(ConnectedEdges.All, connectedGridEdges(3, 7, 3))
        assertFailsWith<IllegalArgumentException> {
            connectedGridEdges(index = 7, size = 7, columns = 3)
        }
        assertFailsWith<IllegalArgumentException> {
            connectedRowEdges(index = 0, size = 0)
        }
    }
}
