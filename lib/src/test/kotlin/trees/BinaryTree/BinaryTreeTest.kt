package trees.BinaryTree

import BinaryTree.BinaryTree
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Duration.ofMillis
import kotlin.random.Random

class BinaryTreeTest {

    @Nested
    inner class `Add check` {
        @Test
        fun `Simple add`() {
            val tree = BinaryTree<Int, Int>()
            val list = listOf(100, 120, 10, -30)
            for (item in list)
                tree.add(item, item)
        }
        @Test
        fun `Equal keys add`() {
            val tree = BinaryTree<Int, String>()
            tree.add(100, "root")
            assertThrows<Exception> { tree.add(100, "abc") }
        }
        @Test
        fun `Multiply add`() {
            assertTimeout(ofMillis(1000)) {
                val tree = BinaryTree<Int, Int>()
                val list : List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()
                for (item in list)
                    tree.add(item, 0)
            }
        }
    }
}