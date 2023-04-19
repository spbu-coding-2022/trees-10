
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import trees.RBTree

class RBTreeTest {
    @Test
    fun testAdd() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        assertEquals("one", tree.search(1)?.value)
        assertEquals("two", tree.search(2)?.value)
        assertEquals("three", tree.search(3)?.value)
    }

    @Test
    fun testSearch() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        assertEquals("one", tree.search(1)?.value)
        assertEquals("two", tree.search(2)?.value)
        assertEquals("three", tree.search(3)?.value)
        assertNull(tree.search(4))
    }

    @Test
    fun testRemove() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        tree.remove(2)
        assertEquals("one", tree.search(1)?.value)
        assertNull(tree.search(2)?.value)
        assertEquals("three", tree.search(3)?.value)
    }

    @Test
    fun testAddSearchRemove() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        assertEquals("one", tree.search(1)?.value)
        assertEquals("two", tree.search(2)?.value)
        assertEquals("three", tree.search(3)?.value)
        tree.remove(2)
        assertEquals("one", tree.search(1)?.value)
        assertNull(tree.search(2))
        assertEquals("three", tree.search(3)?.value)
        tree.add(4, "four")
        assertEquals("four", tree.search(4)?.value)
        assertNull(tree.search(2))
    }

    @Test
    fun testAddMany() {
        val tree = RBTree<Int, String>()
        for (i in 1..1000) {
            tree.add(i, i.toString())
        }
        for (i in 1..1000) {
            assertEquals(i.toString(), tree.search(i)?.value)
        }
    }
}