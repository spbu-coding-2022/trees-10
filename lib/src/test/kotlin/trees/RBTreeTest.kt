import exceptions.*
import nodes.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import trees.RBTree
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

            assertEquals("root -> left -> left", tree.search(30).value)
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
        @DisplayName("Add one element to root")
        fun `Add one element to root`() {
            tree.add(1)
            tree.add(2)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Balance with three elements add check")
        fun `Balance with three elements check`() {
            tree.add(1)
            tree.add(2)
            tree.add(3)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Balance and repaint adding check")
        fun `Balance and repaint simple check`() {
            tree.add(2)
            tree.add(1)
            tree.add(3)
            tree.add(4)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Complex rotate check")
        fun `Rotate check`() {
            tree.add(2)
            tree.add(3)
            tree.add(4)
            tree.add(5)
            tree.add(1)
            tree.add(7)
            tree.add(6)
            tree.add(9)


            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Complex rotate and repaint check")
        fun `Rotate and repaint check`() {
            tree.add(2)
            tree.add(1)
            tree.add(4)
            tree.add(3)
            tree.add(5)
            tree.add(6)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Random elements add")
        fun `Random elements add`() {
            val list: List<Int> = (List(100000) { Random.nextInt(1, 100000) }).distinct().toMutableList()

            for (item in list)
                tree.add(item, "a")

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Add elements existence check")
        fun `New elements existence check`() {
            tree.add(100, "root")
            tree.add(150)
            tree.add(125)
            tree.add(200)
            tree.add(50)
            tree.add(25)
            tree.add(60)

            assertAll(
                Executable { assertTrue(tree.nodeExists(100, "root")) },
                Executable { assertTrue(tree.nodeExists(150, null)) },
                Executable { assertTrue(tree.nodeExists(125, null)) },
                Executable { assertTrue(tree.nodeExists(200, null)) },
                Executable { assertTrue(tree.nodeExists(50, null)) },
                Executable { assertTrue(tree.nodeExists(25, null)) },
                Executable { assertTrue(tree.nodeExists(60, null)) }
            )
        }

        @Test
        @DisplayName("Equal keys add")
        fun `Equals keys add`() {
            tree.add(100)

            assertThrows(NodeAlreadyExistsException::class.java) { tree.add(100) }
        }
    }

    @Nested
    inner class `Remove check` {
        @Test
        @DisplayName("Root element del")
        fun `Root element del`() {
            tree.add(100, "root")
            tree.add(120)
            tree.add(50)

            tree.remove(100)

            assertFalse(tree.nodeExists(100, "root"))
        }

        @Test
        @DisplayName("Node with no children del")
        fun `Node with no children del`() {
            tree.add(100, "a")
            tree.add(120, "b")

            tree.remove(120)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Node with one children del")
        fun `Node with one children del`() {
            tree.add(100, "a")
            tree.add(120, "b")

            tree.remove(100)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Black node simple remove test")
        fun `Black node simple remove test`() {
            tree.add(100, "P")
            tree.add(120, "S")
            tree.add(50, "Sl")
            tree.add(125, "Sr")
            tree.add(115, "Sr")

            tree.remove(120)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Red node with black children remove test")
        fun `Red node with black children remove test`() {
            tree.add(13)
            tree.add(17)
            tree.add(8)
            tree.add(25)
            tree.add(1)
            tree.add(11)
            tree.add(15)
            tree.add(27)

            tree.remove(17)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Black node with red children remove test")
        fun `Black node with red children remove test`() {
            tree.add(13)
            tree.add(17)
            tree.add(8)
            tree.add(25)
            tree.add(1)
            tree.add(11)

            tree.remove(8)

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Random tree element del")
        @RepeatedTest(10)
        fun `Random tree element del`() {
            val list: List<Int> = (List(1000) { Random.nextInt(1, 100000) }).distinct().toMutableList()

            for (item in list)
                tree.add(item, "a")

            tree.remove(list.asSequence().shuffled().first())

            assertTrue(tree.rulesCheck())
        }

        @Test
        @DisplayName("Non-existent element del")
        fun `Non-existent element del`() {
            assertThrows(NodeNotFoundException::class.java) { tree.remove(100) }
        }
    }

    /***
     * Проверяет ноду на существование
     * @return True - найден узел с совпадающим ключом и значением
     */
    private fun <K : Comparable<K>, V> RBTree<K, V>.nodeExists(key: K, value: V?): Boolean {
        if (this.root == null)
            return false
        val searchRes = this.root?.recursiveSearch(key)
        return searchRes != null && searchRes.value == value
    }

    /**
     * Выполняет рекурсивный поиск ноды
     * (нужна чтобы тесты не опирались на ф-ию поиска класса BinaryNode)
     */
    private fun <K : Comparable<K>, V> RBNode<K, V>.recursiveSearch(key: K): RBNode<K, V>? =
        when (key.compareTo(this.key)) {
            1 -> this.right?.recursiveSearch(key)
            0 -> this
            -1 -> this.left?.recursiveSearch(key)
            else -> null
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
        //if (this.root?.checkEndNodesColor() == false)
            //throw TreeException("Конечные узлы RBTree не могут быть красными")
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

        mCount += leftCount

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