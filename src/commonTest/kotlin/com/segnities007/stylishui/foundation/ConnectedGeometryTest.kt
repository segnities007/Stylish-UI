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
    fun `edges suppress internal boundaries and validate their input`() {
        assertEquals(
            ConnectedEdges(top = false, end = false, bottom = false, start = false),
            ConnectedEdges.None,
        )
        assertEquals(
            ConnectedEdges(top = true, end = false, bottom = true, start = true),
            connectedRowEdges(0, 2),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = true, start = true),
            connectedColumnEdges(1, 2),
        )
        // size=7, columns=3: index 3 is the first column of the middle row — right (4),
        // above (0), and below (the partially filled final row is stretched) neighbors exist,
        // but it has no left neighbor, so only its start edge stays enabled.
        assertEquals(
            ConnectedEdges(top = false, end = false, bottom = false, start = true),
            connectedGridEdges(3, 7, 3),
        )
        assertFailsWith<IllegalArgumentException> {
            connectedGridEdges(index = 7, size = 7, columns = 3)
        }
        assertFailsWith<IllegalArgumentException> {
            connectedRowEdges(index = 0, size = 0)
        }
    }

    @Test
    fun `row edges keep outer sides and suppress shared sides`() {
        assertEquals(ConnectedEdges.All, connectedRowEdges(0, 1))
        assertEquals(
            ConnectedEdges(top = true, end = false, bottom = true, start = true),
            connectedRowEdges(0, 2),
        )
        assertEquals(
            ConnectedEdges(top = true, end = false, bottom = true, start = false),
            connectedRowEdges(1, 3),
        )
        assertEquals(
            ConnectedEdges(top = true, end = true, bottom = true, start = false),
            connectedRowEdges(2, 3),
        )
    }

    @Test
    fun `column edges keep outer sides and suppress shared sides`() {
        assertEquals(ConnectedEdges.All, connectedColumnEdges(0, 1))
        assertEquals(
            ConnectedEdges(top = true, end = true, bottom = false, start = true),
            connectedColumnEdges(0, 2),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = true, start = true),
            connectedColumnEdges(1, 2),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = false, start = true),
            connectedColumnEdges(1, 3),
        )
    }

    @Test
    fun `grid edges suppress shared sides at every boundary`() {
        assertEquals(ConnectedEdges.All, connectedGridEdges(0, 1, 3))
        assertEquals(
            ConnectedEdges(top = true, end = false, bottom = false, start = true),
            connectedGridEdges(0, 4, 2),
        )
        assertEquals(
            ConnectedEdges(top = true, end = true, bottom = false, start = false),
            connectedGridEdges(1, 4, 2),
        )
        assertEquals(
            ConnectedEdges(top = false, end = false, bottom = true, start = true),
            connectedGridEdges(2, 4, 2),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = true, start = false),
            connectedGridEdges(3, 4, 2),
        )
        assertEquals(
            ConnectedEdges(top = false, end = false, bottom = false, start = false),
            connectedGridEdges(4, 9, 3),
        )
    }

    @Test
    fun `grid edges treat the stretched final row as a full row of neighbors`() {
        // size=7, columns=3: the middle row (indices 3-5) has a stretched neighbor below in
        // every column, so all its internal edges are suppressed; index 3 keeps its start
        // edge because it is the first column of the grid.
        assertEquals(
            ConnectedEdges(top = false, end = false, bottom = false, start = true),
            connectedGridEdges(3, 7, 3),
        )
        assertEquals(
            ConnectedEdges(top = false, end = false, bottom = false, start = false),
            connectedGridEdges(4, 7, 3),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = true, start = true),
            connectedGridEdges(6, 7, 3),
        )
        // size=3, columns=2: the item above the stretched final row keeps only its outer
        // top/end edges; the stretched item itself keeps its outer bottom and end edges.
        assertEquals(
            ConnectedEdges(top = true, end = true, bottom = false, start = false),
            connectedGridEdges(1, 3, 2),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = true, start = true),
            connectedGridEdges(2, 3, 2),
        )
    }

    @Test
    fun `grid edges with a single column behave like column edges`() {
        assertEquals(
            ConnectedEdges(top = true, end = true, bottom = false, start = true),
            connectedGridEdges(0, 3, 1),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = false, start = true),
            connectedGridEdges(1, 3, 1),
        )
        assertEquals(
            ConnectedEdges(top = false, end = true, bottom = true, start = true),
            connectedGridEdges(2, 3, 1),
        )
    }
}
