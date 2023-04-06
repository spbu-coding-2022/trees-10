package BinaryTree

import BinaryNode

class BinaryTree<T: Comparable<T>, NodeType> {
    private var root : BinaryNode<T, NodeType>? = null
    fun search(key : T) : NodeType? = root?.search(key)
    fun remove(key: T)  {
        root = root?.remove(this.root, key)
    }
    fun add(key : T, value : NodeType) {
        if (root == null)
            root = BinaryNode(key, value, null)
        else {
            root!!.insert(key, value)
        }
    }
}