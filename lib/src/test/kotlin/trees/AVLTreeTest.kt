package trees

import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import nodes.AVLNode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.time.Duration.ofMillis
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.BeforeTest

class AVLTreeTest {

    private lateinit var tree: AVLTree<Int, String>

    @BeforeTest
    fun init() {
        tree = AVLTree()
    }


    @Nested
    inner class `Remove check` {
        @Test
        @DisplayName("Root remove")
        fun `Root remove`() {
            tree.add(100, "root")
            tree.remove(100)
            assertEquals(null, tree.testSearch(100)?.value)
        }

        @Test
        @DisplayName("Non-existence element remove check")
        fun `Non-existence element remove check`() {
            assertThrows(NodeNotFoundException::class.java) { tree.remove(100) }
        }

        @Test
        @DisplayName("Simple element remove")
        fun `Simple element remove`() {
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14, "b")
            tree.add(13)
            tree.remove(13)
            assertAll("elements",
                Executable { assertEquals("root", tree.testSearch(8)?.value) },
                Executable { assertEquals("a", tree.testSearch(10)?.value) },
                Executable { assertEquals("b", tree.testSearch(14)?.value) },
                Executable { assertEquals(null, tree.testSearch(13)?.value) },
                Executable { assertEquals(true, invariantCheck(tree.testSearch(10))) }
            )
        }

        @Test
        @DisplayName("Element with one child node remove")
        fun `Element with one child node remove`() {
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14)
            tree.add(13, "c")
            tree.remove(14)
            assertAll("elements",
                Executable { assertEquals("root", tree.testSearch(8)?.value) },
                Executable { assertEquals("a", tree.testSearch(10)?.value) },
                Executable { assertEquals("c", tree.testSearch(13)?.value) },
                Executable { assertEquals(null, tree.testSearch(14)?.value) },
                Executable { assertEquals(true, invariantCheck(tree.testSearch(10))) }
            )
        }

        @Test
        @DisplayName("Element with two child nodes remove")
        fun `Element with two child nodes remove`() {
            tree.add(8, "root")
            tree.add(4, "a")
            tree.add(1, "b")
            tree.add(6, "c")
            tree.add(7, "d")
            tree.add(3, "e")

            tree.remove(3)
            assertAll("elements",
                Executable { assertEquals("a", tree.testSearch(4)?.value) },
                Executable { assertEquals("b", tree.testSearch(1)?.value) },
                Executable { assertEquals(null, tree.testSearch(3)?.value) },
                Executable { assertEquals("c", tree.testSearch(6)?.value) },
                Executable { assertEquals("d", tree.testSearch(7)?.value) },
                Executable { assertEquals("root", tree.testSearch(8)?.value) },
                Executable { assertEquals(true, invariantCheck(tree.testSearch(4))) }
            )
        }
    }

    @Nested
    inner class `Add check` {
        @Test
        @DisplayName("Simple add")
        fun `Simple add`() {
            tree.add(30, "root")
            assertEquals("root", tree.testSearch(30)?.value)
        }

        @Test
        @DisplayName("Non-existence element remove check")
        fun `Non-existence element remove check`() {
            tree.add(100)

            assertThrows(NodeAlreadyExistsException::class.java) { tree.add(100) }
        }

        @Test
        @DisplayName("Left rotation on add")
        fun `Left rotation on add`() {
            tree.add(1, "root")
            tree.add(2, "a")
            tree.add(3, "b")
            assertEquals("root", tree.testSearch(2)?.left?.value)
            assertEquals(true, invariantCheck(tree.testSearch(2)))
        }

        @Test
        @DisplayName("Right rotation on add")
        fun `Right rotation on add`() {
            tree.add(3, "root")
            tree.add(2, "a")
            tree.add(1, "b")
            assertEquals("root", tree.testSearch(2)?.right?.value)
            assertEquals(true, invariantCheck(tree.testSearch(2)))
        }

        @Test
        @DisplayName("Multiply add")
        fun `Multiply add`() {
            assertTimeout(ofMillis(1000)) {
                val list: List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()
                for (item in list)
                    tree.add(item, "0")
                assertEquals(tree.testSearch(list.last())?.value, "0")
            }
        }

    }

    @Nested
    inner class `Search check` {
        @Test
        @DisplayName("Non-existence element search")
        fun `Non-existence element search`() {
            assertThrows(NodeNotFoundException::class.java) {
                tree.search(100)
            }
        }

        @Test
        @DisplayName("Existence element search")
        fun `Existence element search`() {
            tree.add(100, "root")

            assertEquals("root", tree.search(100).value)
        }
    }

    private fun <K : Comparable<K>, V> AVLTree<K, V>.testSearch(key: K): AVLNode<K, V>? {
        fun <K : Comparable<K>, V> AVLNode<K, V>.recursiveSearch(key: K): AVLNode<K, V>? =
            when (key.compareTo(this.key)) {
                1 -> this.right?.recursiveSearch(key)
                0 -> this
                -1 -> this.left?.recursiveSearch(key)
                else -> null
            }
        return this.root?.recursiveSearch(key)
    }

    private fun getBalance(node: AVLNode<Int, String>?): Int {
        return if (node == null)
            0
        else getHeight(node.left) - getHeight(node.right)
    }

    private fun getHeight(node: AVLNode<Int, String>?): Int = node?.height ?: 0
    fun invariantCheck(node: AVLNode<Int, String>?): Boolean {
        return if (node == null)
            true
        else abs(getBalance(node)) <= 1 && invariantCheck(node.left) && invariantCheck(node.right)
    }
}
