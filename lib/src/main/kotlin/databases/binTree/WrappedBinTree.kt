package databases.binTree

import exceptions.NodeNotFoundException
import exceptions.NullNodeException
import nodes.BinaryNode
import trees.BinaryTree
class WrappedBinTree<K : Comparable<K>, V>() {

    // Список с расширенными нодами
    private var wrappedNodesList: MutableList<WrappedBinNode<K, V>> = mutableListOf()

    // Добавление бинарного дерева
    constructor(tree : BinaryTree<K, V>) : this() {
        if (tree.root != null)
            addNode(tree.root ?: throw NullNodeException())
    }

    /**
     * Позволяет получить сохранённое в классе бинарное дерево
     */
    fun getBinaryTree(): BinaryTree<K, V> {
            val resBinTree = BinaryTree<K, V>()
            for (item in wrappedNodesList)
                resBinTree.add(item.key, item.value)
            return resBinTree
        }

    fun setCoordinate(key: K, x: Double, y: Double) {
        for (item in wrappedNodesList) {
            if (item.key == key) {
                item.x = x
                item.y = y
                return
            }
        }

        // Если ключ не совпадает ни с одной вершиной из имеющихся
        throw NodeNotFoundException()
    }

    fun getWrappedNodesArray(key: K): Array<WrappedBinNode<K, V>> = wrappedNodesList.toTypedArray()

    private fun addNode(node: BinaryNode<K, V>) {
        this.wrappedNodesList.add(WrappedBinNode(node.key, node.value))
        if (node.left != null)
            addNode(node.left ?: throw NullNodeException())
        if (node.right != null)
            addNode(node.right ?: throw NullNodeException())
    }

}