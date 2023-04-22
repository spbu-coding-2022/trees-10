package databases.binTree

import exceptions.NodeNotFoundException
import exceptions.NullNodeException
import nodes.BinaryNode
import trees.AbstractTree
import trees.BinaryTree

data class Node<NodeType>(val binNode: NodeType, val x: Double, val y: Double)

class WrappedBinTree<K : Comparable<K>, V>() {

    private var wrappedNodesList: MutableList<WrappedBinNode<K, V>> = mutableListOf()
    fun addTree(tree: BinaryTree<K, V>) {
        if (tree.root == null)
            return

        addNode(tree.root ?: throw NullNodeException())
    }

    fun getTree(): BinaryTree<K, V> {
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

    fun getWrappedNode(key: K): WrappedBinNode<K, V> {
        for (item in wrappedNodesList)
            if (item.key == key)
                return item

        // Если ключ не совпадает ни с одной вершиной из имеющихся
        throw NodeNotFoundException()
    }

    private fun addNode(node: BinaryNode<K, V>) {
        this.wrappedNodesList.add(WrappedBinNode(node.key, node.value))
        if (node.left != null)
            addNode(node.left ?: throw NullNodeException())
        if (node.right != null)
            addNode(node.right ?: throw NullNodeException())
    }
}