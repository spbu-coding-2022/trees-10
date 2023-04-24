import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import exceptions.NullNodeException
import exceptions.TreeException
import nodes.BinaryNode
import nodes.Color
import nodes.RBNode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import trees.RBTree
import java.time.Duration.ofMillis
import kotlin.random.Random
import kotlin.test.BeforeTest

class RBTreeTest {

    private lateinit var tree: RBTree<Int, String>

    @BeforeTest
    fun init() {
        tree = RBTree()
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

            assertEquals("root -> left -> left", tree.search(30)?.value)
        }

        @Test
        @DisplayName("Not-existent elements search")
        fun `Not-existent elements search`() {
            assertThrows(NodeNotFoundException::class.java) { tree.search(100) }
        }
    }

    @Nested
    inner class `Add check` {
        @Test
        @DisplayName("Root element add")
        fun `Root element add`() {
            tree.add(100)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Small tree create test")
        fun `Small tree create test`() {
            tree.add(100)
            tree.add(150)
            tree.add(10)
            tree.add(0)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Add to different subtrees")
        fun `Add to different subtrees`() {
            tree.add(100, "root")
            tree.add(150)
            tree.add(125)
            tree.add(200)
            tree.add(50)
            tree.add(25)
            tree.add(60)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Equal keys add")
        fun `Equals keys add`() {
            tree.add(100)

            assertThrows(NodeAlreadyExistsException::class.java) { tree.add(100) }
        }

        @Test
        @DisplayName("Equal keys add")
        fun `Equal keys add`() {
            tree.add(100)

            assertThrows(NodeAlreadyExistsException::class.java) { tree.add(100) }
        }
    }

    /**
     * Проверка дерева на соответствие правилам красно-чёрных деревьев
     * @return True - узлы размещены как положено
     * */
    private fun <K : Comparable<K>, V> RBTree<K, V>.rulesCheck(): Boolean {

        if (this.root?.childrenCheck() == false)
            throw TreeException("Неверное расположение узлов, key слева должен быть меньше, а key справа - больше")
        if (this.root?.color != Color.BLACK)
            throw TreeException("root у RBTree не можешь быть красным")
        if (this.root?.checkEndNodesColor() == false)
            throw TreeException("Конечные узлы RBTree не могут быть красными")
        if (this.root?.checkEndNodesColor() == false)
            throw TreeException("У красного узла родительский узел может быть только чёрным")

        this.root?.getBlackNodesCount() // Если что само выбросит исключение

        return true
    }

    /**
     * Проверка на то, что левый узел меньше, а правый больше
     * @return True - узлы размещены как положено
     * */
    private fun <K : Comparable<K>, V> RBNode<K, V>.childrenCheck(): Boolean =
        (this.left == null || this.left?.compareTo(this) == -1)
                && (this.right == null || this.right?.compareTo(this) == 1)
                && (this.right == null || right?.childrenCheck() == true)
                && (this.left == null || left?.childrenCheck() == true)

    /**
     * Проверяет, что у каждого красного узла родительский узел - чёрный
     * @return True - условие верно
     */
    private fun <K : Comparable<K>, V> RBNode<K, V>.checkNodeParentColor(): Boolean {
        return if (this.color == Color.RED && this.parent?.color != Color.BLACK)
            false
        else
            (this.right == null || this.right?.checkEndNodesColor() == true) &&
                    (this.left == null || this.left?.checkEndNodesColor() == true)
    }

    /**
     * @return Число чёрных нод на пути из ноды в лист
     */
    private fun <K : Comparable<K>, V> RBNode<K, V>.getBlackNodesCount(count: Int = 0): Int {
        var mCount = count
        if (this.color == Color.BLACK)
            mCount++
        var leftCount = 0
        var rightCount = 0
        if (this.left != null)
            leftCount += this.left?.getBlackNodesCount() ?: throw NullNodeException()
        if (this.right != null)
            rightCount += this.right?.getBlackNodesCount() ?: throw NullNodeException()

        // Теперь смотрим, выполняется ли условие
        if (leftCount != rightCount)
            throw TreeException("Узел [${this.key}, ${this.value}] содержит разное кол-во чёрных узлов в левом [$leftCount] и правом [$rightCount] поддереве")

        mCount += leftCount + rightCount

        return mCount
    }

    /**
     * Проверяет что конечные узлы чёрные
     * @return True - всё верно
     */
    private fun <K : Comparable<K>, V> RBNode<K, V>.checkEndNodesColor(): Boolean {
        return if (this.right == null && this.left == null && this.color == Color.BLACK)
            true
        else
            (this.right == null || this.right?.checkEndNodesColor() == true) &&
                    (this.left == null || this.right?.checkEndNodesColor() == true)
    }

}