package nodes

import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import exceptions.NullNodeException

/**
 * Класс узла для бинарного дерева.
 *
 * Позволяет рекурсивно реализовывать операции *поиска*, *удаления*, *добавления*.
 *
 * @property key ключ для поиска.
 * @property value значение узла.
 * @author Dmitriy Zaytsev
 */
class BinaryNode<K : Comparable<K>, V>(key: K, value: V?) :
    AbstractNode<K, V, BinaryNode<K, V>>(key, value) {
    override var color: Color = Color.DARK_GRAY
    fun search(key: K): BinaryNode<K, V>? =
        when (key.compareTo(this.key)) {
            1 -> this.right?.search(key)
            0 -> this
            -1 -> this.left?.search(key)
            else -> null
        }
    fun remove(root: BinaryNode<K, V>?, key: K): BinaryNode<K, V>? {
        if (key == this.key) { // когда remove вызывается для удаляемой вершины
            if (right == null && left == null)
                return null // просто стираем ноду

            // Простой случай - если есть только 1 потомок
            else if (left == null)
                return right
            else if (right == null)
                return left

            // Случай где есть два потомка
            else {
                // Находим минимальное дерево
                val minNode = findMin(right) ?: throw NullNodeException()

                // Перенимаем его key и value
                minNode.let {
                    this.key = it.key
                    this.value = it.value
                    // Удаляем минимальное дерево
                    this.right = right?.remove(right, it.key)
                }
                return this
            }

        } else {
            if (left == null && right == null)
                throw NodeNotFoundException()
            // Идём дальше по дереву искать что удалить
            if (key < this.key)
                this.left = left?.remove(left, key)
            else
                this.right = right?.remove(right, key)
            return root
        }
    }

    fun add(key: K, value: V?) {
        val compare = key.compareTo(this.key)

        if (compare == 1) {
            if (right == null)
                right = BinaryNode(key, value)
            else
                right?.add(key, value)
        } else if (compare == 0)
        // Попытка добавления новой ноды с уже существующим в дереве ключом
            throw NodeAlreadyExistsException()
        else {
            if (left == null)
                left = BinaryNode(key, value)
            else
                left?.add(key, value)
        }
    }

    /**
     * @param[node] Узел для которого ищется минимальный эл-т.
     * @return Наименьший узел.
     */
    private fun findMin(node: BinaryNode<K, V>?): BinaryNode<K, V>? {
        return if (node?.left != null)
            findMin(node.left)
        else
            node
    }
}
