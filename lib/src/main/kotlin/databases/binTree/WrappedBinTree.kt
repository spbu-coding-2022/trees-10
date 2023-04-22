package databases.binTree

import trees.AbstractTree
import trees.BinaryTree

data class Node<NodeType>(val binNode: NodeType, val x: Double, val y: Double)

class WrappedBinTree<K : Comparable<K>, V> : AbstractTree<K, V, WrappedBinNode<K, V>>() {
    override fun search(key: K): WrappedBinNode<K, V>? {
        TODO("Not yet implemented")
    }

    override fun add(key: K, value: V?) {
        TODO("Not yet implemented")
    }

    override fun remove(key: K) {
        TODO("Not yet implemented")
    }
}