package databases.binTree

import exceptions.NodeNotFoundException
import exceptions.NullNodeException
import nodes.BinaryNode
import trees.BinaryTree

class WrappedBinTree<K : Comparable<K>, V>() {

    // Список с расширенными нодами
    private var wrappedNodesList: MutableList<WrappedBinNode<K, V>> = mutableListOf()

    // Добавление бинарного дерева
    constructor(tree: BinaryTree<K, V>) : this() {
        if (tree.root != null)
            addNodeToWrappedList(tree.root ?: throw NullNodeException())
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

    /**
     * Позволяет добавить новую ноду в дерево
     */
    fun add(key: K, value: V?) {
        val newTree = getBinaryTree()
        newTree.add(key, value)

        wrappedNodesList.clear()

        newTree.root?.let { addNodeToWrappedList(it) }
    }
    fun add(node: BinaryNode<K, V>) = add(node.key, node.value)
    fun add(node: WrappedBinNode<K, V>) {
        add(node.key, node.value)
        setCoordinate(node.key, node.x, node.y)
    }
    fun remove(key: K) {
        val newTree = getBinaryTree()
        newTree.remove(key)

        wrappedNodesList.clear()

        newTree.root?.let { addNodeToWrappedList(it) }
    }
    fun search(key: K): WrappedBinNode<K, V> {
        for (item in wrappedNodesList)
            if (item.key == key)
                return item
        throw NodeNotFoundException()
    }
    fun getWrappedNodesArray(key: K): Array<WrappedBinNode<K, V>> = wrappedNodesList.toTypedArray()

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

    private fun addNodeToWrappedList(node: BinaryNode<K, V>) {
        this.wrappedNodesList.add(WrappedBinNode(node.key, node.value))
        if (node.left != null)
            addNodeToWrappedList(node.left ?: throw NullNodeException())
        if (node.right != null)
            addNodeToWrappedList(node.right ?: throw NullNodeException())
    }

}