package trees

import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import nodes.BinaryNode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.time.Duration.ofMillis
import kotlin.random.Random
import kotlin.test.BeforeTest

class BinaryTreeTest {

    private lateinit var tree: BinaryTree<Int, String>

    @BeforeTest
    fun init() {
        tree = BinaryTree()
    }

    @Nested
    inner class `Search check` {
        @Test
        @DisplayName("Existing elements search")
        fun `Existing elements search`() {
            tree.add(100, "root")
            tree.add(150, "root -> right")
            tree.add(50, "root -> left")
            tree.add(30, "root -> left -> left")

            assertAll(
                Executable { assertEquals("root", tree.search(100).value) },
                Executable { assertEquals("root -> right", tree.search(150).value) },
                Executable { assertEquals("root -> left", tree.search(50).value) },
                Executable { assertEquals("root -> left -> left", tree.search(30).value) })
        }

        @Test
        @DisplayName("Not-existent elements search")
        fun `Not-existent elements search`() {
            assertThrows(NodeNotFoundException::class.java) { tree.search(100) }
        }
    }

    @Nested
    inner class `Remove check` {

        @Test
        @DisplayName("Root remove")
        fun `Root remove`() {
            tree.add(100, "root")

            tree.remove(100)

            assertFalse(tree.nodeExists(100, null))
        }

        @Test
        @DisplayName("Node without children remove")
        fun `Remove node without children`() {
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14, "b")
            tree.add(13)

            tree.remove(13)

            // Проверка что каждый assertEquals не выбрасывает исключений
            // В случае исключения выведется сообщение о нём
            assertAll("elements",
                Executable { assertTrue(tree.nodeExists(8, "root")) },
                Executable { assertTrue(tree.nodeExists(10, "a")) },
                Executable { assertTrue(tree.nodeExists(14, "b")) },
                Executable { assertFalse(tree.nodeExists(13, null)) },
                Executable { assertTrue(tree.root?.childrenCheck() ?: false) }
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
                Executable { assertTrue(tree.nodeExists(8, "root")) },
                Executable { assertTrue(tree.nodeExists(10, "a")) },
                Executable { assertTrue(tree.nodeExists(13, "c")) },
                Executable { assertFalse(tree.nodeExists(14, null)) },
                Executable { assertTrue(tree.root?.childrenCheck() ?: false) }
            )
        }

        @Test
        @DisplayName("Non-existent element remove")
        fun `Non-existent element remove`() {
            tree.add(100)
            tree.add(120)
            tree.add(130)
            tree.add(109)
            assertThrows(NodeNotFoundException::class.java) { tree.remove(150) }
        }

        @Test
        @DisplayName("Non-existent root remove")
        fun `Non-existent root remove`() {
            assertThrows(NodeNotFoundException::class.java) { tree.remove(100) }
        }

        @Test
        @DisplayName("Element with two child nodes remove")
        fun `Element with two child nodes remove`() {
            tree.add(8, "root")
            tree.add(10, "a")
            tree.add(14)
            tree.add(13)
            tree.add(3, "abc")
            tree.add(1, "b")
            tree.add(6, "c")
            tree.add(4)
            tree.add(7, "d")

            tree.remove(3)

            assertAll("elements",
                Executable { assertTrue(tree.nodeExists(10, "a")) },
                Executable { assertTrue(tree.nodeExists(1, "b")) },
                Executable { assertFalse(tree.nodeExists(3, "abc")) },
                Executable { assertTrue(tree.nodeExists(6, "c")) },
                Executable { assertTrue(tree.nodeExists(7, "d")) },
                Executable { assertTrue(tree.nodeExists(8, "root")) },
                Executable { assertTrue(tree.root?.childrenCheck() ?: false) }
            )
        }
    }

    @Nested
    inner class `Add Check` {
        @Test
        @DisplayName("Root add")
        fun `Root add`() {
            tree.add(30, "root")

            assertTrue(tree.nodeExists(30, "root"))
        }

        @Test
        @DisplayName("Equal keys add")
        fun `Equal keys add`() {
            tree.add(100)

            assertThrows(NodeAlreadyExistsException::class.java) { tree.add(100) }
        }

        @Test
        @DisplayName("Add to different nodes")
        fun `Add to different subtrees`() {
            tree.add(100, "root")
            tree.add(150, "root -> right")
            tree.add(80, "root -> left")

            assertAll("elements",
                Executable { assertTrue(tree.nodeExists(100, "root")) },
                Executable { assertTrue(tree.nodeExists(150, "root -> right")) },
                Executable { assertTrue(tree.nodeExists(80, "root -> left")) },
                Executable { assertTrue(tree.root?.childrenCheck() ?: false) }
            )
        }

        @Test
        @DisplayName("Large number of elements add")
        fun `Add a large number of elements`() {
            assertTimeout(ofMillis(1000)) {
                val list: List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()

                for (item in list)
                    tree.add(item, "a")
            }
        }
    }

    /**
     * Проверка на то, что левый узел меньше, а правый больше
     * @return True - узлы размещены как положено
     * */
    private fun <K : Comparable<K>, V> BinaryNode<K, V>.childrenCheck(): Boolean =
        (this.left == null || this.left?.compareTo(this) == -1)
                && (this.right == null || this.right?.compareTo(this) == 1)

    /**
     * Выполняет рекурсивный поиск ноды
     * (нужна чтобы тесты не опирались на ф-ию поиска класса BinaryNode)
     */
    private fun <K : Comparable<K>, V> BinaryNode<K, V>.recursiveSearch(key: K): BinaryNode<K, V>? =
        when (key.compareTo(this.key)) {
            1 -> this.right?.recursiveSearch(key)
            0 -> this
            -1 -> this.left?.recursiveSearch(key)
            else -> null
        }

    /***
     * Проверяет ноду на существование
     * @return True - найден узел с совпадающим ключом и значением
     */
    private fun <K : Comparable<K>, V> BinaryTree<K, V>.nodeExists(key: K, value: V?): Boolean {
        if (this.root == null)
            return false
        val searchRes = this.root?.recursiveSearch(key)
        return searchRes != null && searchRes.value == value
    }
}
