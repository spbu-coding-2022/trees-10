package BinaryNode

/**
 * Класс узла для бинарного дерева.
 *
 * Позволяет рекурсивно реализовывать операции *поиска*, *удаления*, *добавления*.
 *
 * @property key ключ для поиска.
 * @property value значение узла.
 * @author Dmitriy Zaytsev
 */
open class BinaryNode<T: Comparable<T>, NodeType>(key: T, value: NodeType?) : Comparable<BinaryNode<T, NodeType>> {

    open var key : T = key
        protected set
    open var value : NodeType? = value
        protected set

    protected open var right : BinaryNode<T, NodeType>? = null
    protected open var left : BinaryNode<T, NodeType>? = null

    open fun search(key: T) : NodeType? =
        when (key.compareTo(this.key)) {
            1 -> this.right?.search(key)
            0 -> this.value
            -1 -> this.left?.search(key)
            else -> null
    }

    open fun remove(root : BinaryNode<T, NodeType>?, key : T) : BinaryNode<T, NodeType>? {
        if (root == null)
            return null
        if (key == this.key) { // когда remove вызывается для удаляемой вершины
            if (this.right == null && this.left == null)
                return null
            // Простой случай - если есть только 1 потомок
            else if  (this.left == null)
                return this.right
            else if (this.right == null)
                return this.left
            // Есть оба поддерева
            else {
                // Находим минимальное дерево
                // Перенимаем его key и value
                // Удаляем минимальное дерево
                val minNode = findMin(this.right)
                this.key = minNode!!.key
                this.value = minNode.value
                this.right = right!!.remove(this.right, minNode.key)
                return this
            }

        } else {
            // Замещаем поддеревья на минимальные
            if (key < this.key)
                this.left = left?.remove(left, key)
            else
                this.right = right?.remove(right, key)
            return root
        }
    }

    /**
     * @param[node] Узел для которого ищется минимальный эл-т.
     * @return Наименьший узел.
     */
    private fun findMin(node: BinaryNode<T, NodeType>?): BinaryNode<T, NodeType>? = if (node?.left != null) findMin(node.left) else node

    @Throws(Exception::class)
    open fun add(key : T, value : NodeType?) {
        val compare = key.compareTo(this.key)

        if (compare == 1) {
            if (right == null)
                right = BinaryNode(key, value)
            else
                right!!.add(key, value)
        } else if (compare == 0) {
            this.value = value
        } else {
            if (left == null)
                left = BinaryNode(key, value)
            else
                left!!.add(key, value)
        }
    }

    override fun toString(): String {
        return "<$key, $value>"
    }
    override fun compareTo(other: BinaryNode<T, NodeType>): Int = this.key.compareTo(other.key)
}