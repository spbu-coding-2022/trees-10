package trees

import BinaryTree.BinaryTree
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.function.Executable
import java.time.Duration.ofMillis
import kotlin.random.Random

class BinaryTreeTest {


    @Nested
    inner class `Remove check` {

        /**
         * Добавляет последовательно массив из ключей с null value
         */
        private fun <T : Comparable<T>, NodeType> BinaryTree<T, NodeType>.addGroup(keys : Array<T>) {
            for (item in keys)
                this.add(item)
        }
        @Test
        @DisplayName("Root remove")
        fun `Root remove`() {
            val tree = BinaryTree<Int, String>()
            tree.add(100, "root")
            tree.remove(100)
            assertEquals(null, tree.search(100))
        }
        @Test
        @DisplayName("Simple element remove")
        fun `Simple element remove`() {
            val tree = BinaryTree<Int, String>()
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14, "b")
            tree.add(13)
            tree.remove(13)
            assertAll("elements",
                Executable { assertEquals("root", tree.search(8)) },
                Executable { assertEquals("a", tree.search(10)) },
                Executable { assertEquals("b", tree.search(14)) },
                Executable { assertEquals(null, tree.search(13)) }
            )
        }
        @Test
        @DisplayName("Element with one child node remove")
        fun `Element with one child node remove`() {
            val tree = BinaryTree<Int, String>()
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14)
            tree.add(13, "c")
            tree.remove(14)
            assertAll("elements",
                Executable { assertEquals("root", tree.search(8)) },
                Executable { assertEquals("a", tree.search(10)) },
                Executable { assertEquals("c", tree.search(13)) },
                Executable { assertEquals(null, tree.search(14)) }
            )
        }
        @Test
        @DisplayName("Element with two child nodes remove")
        fun `Element with two child nodes remove`() {
            val tree = BinaryTree<Int, String>()
            tree.addGroup(arrayOf(8, 10, 14, 13, 3, 1, 6, 4, 7))
            tree.add(8, "root")
            tree.add(4, "a")
            tree.add(1, "b")
            tree.add(6, "c")
            tree.add(7, "d")

            tree.remove(3)
            assertAll("elements",
                Executable { assertEquals("a", tree.search(4)) },
                Executable { assertEquals("b", tree.search(1)) },
                Executable { assertEquals(null, tree.search(3)) },
                Executable { assertEquals("c", tree.search(6)) },
                Executable { assertEquals("d", tree.search(7)) },
                Executable { assertEquals("root", tree.search(8)) }
            )
        }
    }
    @Nested
    inner class `Add check` {
        @Test
        @DisplayName("Simple add")
        fun `Simple add`() {
            val tree = BinaryTree<Int, String>()
            tree.add(30, "root")
            assertEquals("root", tree.search(30))
        }
        @Test
        @DisplayName("Equal keys add")
        fun `Equal keys add`() {
            val tree = BinaryTree<Int, String>()
            tree.add(100, "abc")
            tree.add(100, "root")
            assertEquals("root", tree.search(100))
        }
        @Test
        @DisplayName("Multiply add")
        fun `Multiply add`() {
            assertTimeout(ofMillis(1000)) {
                val tree = BinaryTree<Int, Int>()
                val list : List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()
                for (item in list)
                    tree.add(item, 0)
                assertEquals(tree.search(list.last()), 0)
            }
        }
    }
}