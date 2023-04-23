package databases.binTree

import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import org.junit.jupiter.api.Assertions.*
import trees.BinaryTree
import nodes.BinaryNode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import kotlin.random.Random
import kotlin.test.BeforeTest

class WrappedBinTreeTest {

    private lateinit var binTree: BinaryTree<Int, String>
    private lateinit var wrappedTree: WrappedBinTree<Int, String>

    @BeforeTest
    fun init() {
        binTree = BinaryTree()
    }

    @Nested
    inner class `Equivalence check` {
        @Test
        @DisplayName("Existing elements search")
        fun `Root equivalence check`() {
            binTree.add(100, "root")
            wrappedTree = WrappedBinTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }

        @Test
        @DisplayName("Tree with one subtree equivalence check")
        fun `Tree with one subtree equivalence check`() {
            binTree.add(100, "root")
            binTree.add(120, "a")
            binTree.add(130, "b")
            binTree.add(140, "c")
            binTree.add(125, "d")
            binTree.add(160, "e")
            wrappedTree = WrappedBinTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }

        @Test
        @DisplayName("Tree with one subtree equivalence check")
        fun `Tree with two subtrees equivalence check`() {
            binTree.add(100, "root")
            binTree.add(150)
            binTree.add(160)
            binTree.add(110)
            binTree.add(50)
            binTree.add(-100)
            binTree.add(89)

            wrappedTree = WrappedBinTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }

        @Test
        @DisplayName("Random tree equivalence check")
        fun `Random tree equivalence check`() {
            val list: List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()

            for (item in list)
                binTree.add(item)

            wrappedTree = WrappedBinTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))

        }

        @Test
        @DisplayName("Null-root tree equivalence check")
        fun `Null-root tree equivalence check`() {
            wrappedTree = WrappedBinTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }
    }

    @Nested
    inner class `Coordinates check` {
        @Test
        @DisplayName("Coordinate change check")
        fun `Coordinate change check`() {
            binTree.add(100, "root")

            wrappedTree = WrappedBinTree(binTree)
            wrappedTree.setNodeCoordinate(100, 10.0, -10.0)

            assertAll("elements",
                Executable { assertTrue(wrappedTree.search(100).x == 10.0)},
                Executable { assertTrue(wrappedTree.search(100).y == -10.0)}
            )
        }

        @Test
        @DisplayName("Coordinate change check")
        fun `Non-existent coordinate change`() {
            binTree.add(100, "root")

            wrappedTree = WrappedBinTree(binTree)

            assertThrows(NodeNotFoundException::class.java) { wrappedTree.setNodeCoordinate(150, 10.0, -10.0) }
        }

    }

    @Nested
    inner class `Add test` {
        @Test
        @DisplayName("One element add test")
        fun `One element add test`() {
            wrappedTree = WrappedBinTree()

            binTree.add(100)
            wrappedTree.add(100)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }

        @Test
        @DisplayName("One element add test")
        fun `Already existent element add test`() {
            wrappedTree = WrappedBinTree()
            wrappedTree.add(100, "root")

            assertThrows(NodeAlreadyExistsException::class.java) { wrappedTree.add(100, "root") }
        }

        @Test
        @DisplayName("Random tree add check")
        fun `Random tree add check`() {
            wrappedTree = WrappedBinTree()

            val list: List<Int> = (List(100) { Random.nextInt(1, 100000) }).distinct().toMutableList()

            for (item in list) {
                wrappedTree.add(item)
                binTree.add(item)
            }

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }
    }

    @Nested
    inner class `Remove test` {
        @Test
        @DisplayName("One element add test")
        fun `One element remove test`() {
            binTree.add(100)
            wrappedTree = WrappedBinTree(binTree)
            binTree.remove(100)
            wrappedTree.remove(100)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }

        @Test
        @DisplayName("Non-existent element remove test")
        fun `Non-existent element remove test`() {
            wrappedTree = WrappedBinTree()

            assertThrows(NodeNotFoundException::class.java) {wrappedTree.remove(100)}
        }

        @Test
        @DisplayName("Random tree add check")
        fun `Random tree remove check`() {
            binTree.add(100)
            binTree.add(200)
            binTree.add(150)
            binTree.add(10)
            binTree.add(220)
            binTree.add(-100)
            binTree.add(89)
            binTree.add(-50)
            binTree.add(-34)

            wrappedTree = WrappedBinTree(binTree)

            wrappedTree.remove(100)
            binTree.remove(100)

            assertTrue(binTree.equalCheck(wrappedTree.getBinaryTree()))
        }
    }

    @Nested
    inner class `Search test` {
        @Test
        @DisplayName("Existent element search")
        fun `Existent element search`() {
            binTree.add(100)

            wrappedTree = WrappedBinTree(binTree)
            wrappedTree.setNodeCoordinate(100, 10.0)

            assertEquals(10.0, wrappedTree.search(100).x)
        }

        @Test
        @DisplayName("Non-existent element search")
        fun `Non-existent element search`() {
            wrappedTree = WrappedBinTree()

            assertThrows(NodeNotFoundException::class.java){ wrappedTree.search(100).x}
        }
    }

    /**
     * Сравнивает переданные ноды по всем параметрам (рекурсивно обходит детей)
     * @return True - ноды полностью совпадают
     */
    private fun <K : Comparable<K>, V> BinaryNode<K, V>?.nodesEqualCheck(other: BinaryNode<K, V>?): Boolean {
        return (this == null && other == null) ||
                ((other?.key == this?.key) &&
                        (other?.value == this?.value) &&
                        (this?.left.nodesEqualCheck(other?.left)) &&
                        (this?.right.nodesEqualCheck(other?.right)))
    }

    /**
     * Сравнивает деревья между собой
     * @return True - деревья полностью схожи (включая расположение детей)
     */
    fun <K : Comparable<K>, V> BinaryTree<K, V>.equalCheck(other: BinaryTree<K, V>): Boolean =
        this.root.nodesEqualCheck(other.root)
}