package databases.binTree

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
        wrappedTree = WrappedBinTree()
    }

    @Nested
    inner class `Equivalence check` {
        @Test
        @DisplayName("Existing elements search")
        fun `Root equivalence check`() {
            binTree.add(100, "root")
            wrappedTree.addTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getTree()))
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
            wrappedTree.addTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getTree()))
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

            wrappedTree.addTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getTree()))
        }

        @Test
        @DisplayName("Random tree equivalence check")
        fun `Random tree equivalence check`() {
            val list: List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()

            for (item in list)
                binTree.add(item)

            wrappedTree.addTree(binTree)

            assertTrue(binTree.equalCheck(wrappedTree.getTree()))

        }
    }

    @Nested
    inner class `Coordinates check` {
        @Test
        @DisplayName("Coordinate change check")
        fun `Coordinate change check`() {
            binTree.add(100, "root")

            wrappedTree.addTree(binTree)
            wrappedTree.setCoordinate(100, 10.0, -10.0)

            assertAll("elements",
                Executable { assertTrue(wrappedTree.getWrappedNode(100).x == 10.0)},
                Executable { assertTrue(wrappedTree.getWrappedNode(100).y == -10.0)}
            )
        }

        @Test
        @DisplayName("Coordinate change check")
        fun `Non-existent coordinate change`() {
            binTree.add(100, "root")

            wrappedTree.addTree(binTree)

            assertThrows(NodeNotFoundException::class.java) { wrappedTree.setCoordinate(150, 10.0, -10.0) }
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