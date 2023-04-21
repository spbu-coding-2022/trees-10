package nodes

/**
 * Класс узла для бинарного дерева.
 *
 * Позволяет рекурсивно реализовывать операции *поиска*, *удаления*, *добавления*.
 *
 * @property key ключ для поиска.
 * @property value значение узла.
 * @author Dmitriy Zaytsev
 */
open class BinaryNode<K: Comparable<K>, V>(override var key: K, override var value: V?)  : AbstractNode<K, V, BinaryNode<K, V>>() {

    override var right : BinaryNode<K, V>? = null
    override var left : BinaryNode<K, V>? = null

    open fun search(key: K) : BinaryNode<K, V>? =
        when (key.compareTo(this.key)) {
            1 -> this.right?.search(key)
            0 -> this
            -1 -> this.left?.search(key)
            else -> null
    }

    open fun remove(root : BinaryNode<K, V>?, key : K) : BinaryNode<K, V>? {
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
                minNode?.let {
                    this.key = it.key
                    this.value = it.value
                }
                this.right = right?.remove(right, key)
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
    private fun findMin(node: BinaryNode<K, V>?): BinaryNode<K, V>? = if (node?.left != null) findMin(node.left) else node

    open fun add(key : K, value : V?) {
        val compare = key.compareTo(this.key)

        if (compare == 1) {
            if (right == null)
                right = BinaryNode(key, value)
            else
                right?.add(key, value)
        } else if (compare == 0) {
            this.value = value
        } else {
            if (left == null)
                left = BinaryNode(key, value)
            else
                left?.add(key, value)
        }
    }

}