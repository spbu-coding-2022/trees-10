import org.junit.Assert.*
import org.junit.Test
import RBTree

class RBTreeTest {
    @Test
    fun testAdd() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        assertEquals("one", tree.search(1))
        assertEquals("two", tree.search(2))
        assertEquals("three", tree.search(3))
    }

    @Test
    fun testSearch() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        assertEquals("one", tree.search(1))
        assertEquals("two", tree.search(2))
        assertEquals("three", tree.search(3))
        assertNull(tree.search(4))
    }

    @Test
    fun testRemove() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        tree.remove(2)
        assertEquals("one", tree.search(1))
        assertNull(tree.search(2))
        assertEquals("three", tree.search(3))
    }

    @Test
    fun testAddSearchRemove() {
        val tree = RBTree<Int, String>()
        tree.add(1, "one")
        tree.add(2, "two")
        tree.add(3, "three")
        assertEquals("one", tree.search(1))
        assertEquals("two", tree.search(2))
        assertEquals("three", tree.search(3))
        tree.remove(2)
        assertEquals("one", tree.search(1))
        assertNull(tree.search(2))
        assertEquals("three", tree.search(3))
        tree.add(4, "four")
        assertEquals("four", tree.search(4))
        assertNull(tree.search(2))
    }

    @Test
    fun testAddMany() {
        val tree = RBTree<Int, String>()
        for (i in 1..1000) {
            tree.add(i, i.toString())
        }
        for (i in 1..1000) {
            assertEquals(i.toString(), tree.search(i))
        }
    }
}