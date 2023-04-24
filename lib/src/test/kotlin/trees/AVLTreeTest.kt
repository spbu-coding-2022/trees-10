package trees

import exceptions.NodeAlreadyExistsException
import nodes.AVLNode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.time.Duration.ofMillis
import kotlin.math.abs
import kotlin.random.Random

class AVLTreeTest {
    @Nested
    inner class `Remove check` {
        @Test
        @DisplayName("Root remove")
        fun `Root remove`() {
            val tree = AVLTree<Int, String>()
            tree.add(100, "root")
            tree.remove(100)
            assertEquals(null, tree.search(100)?.value)
        }
        @Test
        @DisplayName("Non-existence element remove check")
        fun `Non-existence element remove check`() {
            assertThrows(NodeAlreadyExistsException::class.java) { tree.remove() }
        }

        @Test
        @DisplayName("Simple element remove")
        fun `Simple element remove`() {
            val tree = AVLTree<Int, String>()
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14, "b")
            tree.add(13)
            tree.remove(13)
            assertAll("elements",
                Executable { assertEquals("root", tree.search(8)?.value) },
                Executable { assertEquals("a", tree.search(10)?.value) },
                Executable { assertEquals("b", tree.search(14)?.value) },
                Executable { assertEquals(null, tree.search(13)?.value) },
                Executable { assertEquals(true, invariantCheck(tree.search(10))) }
            )
        }
        @Test
        @DisplayName("Element with one child node remove")
        fun `Element with one child node remove`() {
            val tree = AVLTree<Int, String>()
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14)
            tree.add(13, "c")
            tree.remove(14)
            assertAll("elements",
                Executable { assertEquals("root", tree.search(8)?.value) },
                Executable { assertEquals("a", tree.search(10)?.value) },
                Executable { assertEquals("c", tree.search(13)?.value) },
                Executable { assertEquals(null, tree.search(14)?.value) },
                Executable { assertEquals(true, invariantCheck(tree.search(10))) }
            )
        }
        @Test
        @DisplayName("Element with two child nodes remove")
        fun `Element with two child nodes remove`() {
            val tree = AVLTree<Int, String>()
            tree.add(8, "root")
            tree.add(4, "a")
            tree.add(1, "b")
            tree.add(6, "c")
            tree.add(7, "d")

            tree.remove(3)
            assertAll("elements",
                Executable { assertEquals("a", tree.search(4)?.value) },
                Executable { assertEquals("b", tree.search(1)?.value) },
                Executable { assertEquals(null, tree.search(3)?.value) },
                Executable { assertEquals("c", tree.search(6)?.value) },
                Executable { assertEquals("d", tree.search(7)?.value) },
                Executable { assertEquals("root", tree.search(8)?.value) },
                Executable { assertEquals(true, invariantCheck(tree.search(4))) }
            )
        }
    }

    @Nested
    inner class `Add check` {
        @Test
        @DisplayName("Simple add")
        fun `Simple add`() {
            val tree = AVLTree<Int, String>()
            tree.add(30, "root")
            assertEquals("root", tree.search(30)?.value)
        }
        @Test
        @DisplayName("Left rotation on add")
        fun `Left rotation on add`() {
            val tree = AVLTree<Int, String>()
            tree.add(1, "root")
            tree.add(2, "a")
            tree.add(3, "b")
            assertEquals("root", tree.search(2)?.left?.value)
            assertEquals(true, invariantCheck(tree.search(2)))
        }

        @Test
        @DisplayName("Right rotation on add")
        fun `Right rotation on add`() {
            val tree = AVLTree<Int, String>()
            tree.add(3, "root")
            tree.add(2, "a")
            tree.add(1, "b")
            assertEquals("root", tree.search(2)?.right?.value)
            assertEquals(true, invariantCheck(tree.search(2)))
        }

        @Test
        @DisplayName("Multiply add")
        fun `Multiply add`() {
            assertTimeout(ofMillis(1000)) {
                val tree = AVLTree<Int, Int>()
                val list : List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()
                for (item in list)
                    tree.add(item, 0)
                assertEquals(tree.search(list.last())?.value, 0)
            }
        }

    }

    private fun getBalance(node: AVLNode<Int, String>?): Int {
        if (node == null) {
            return 0
        }
        return getHeight(node.left) - getHeight(node.right)
    }

    private fun getHeight(node: AVLNode<Int, String>?): Int {
        if (node == null) {
            return 0
        }
        return node.height
    }

    fun invariantCheck(node: AVLNode<Int, String>?): Boolean {
        if (node == null) {
            return true
        }
        return abs(getBalance(node)) <= 1 && invariantCheck(node.left) && invariantCheck(node.right)
    }
}
