package BinaryTree

import BinaryNode.BinaryNode

open class BinaryTree<T: Comparable<T>, NodeType> {
    protected open var root : BinaryNode<T, NodeType>? = null
    open fun search(key : T) : NodeType? = root?.search(key)
    open fun remove(key: T)  {
        root = root?.remove(this.root, key)
    }
    open fun add(key : T, value : NodeType) {
        if (root == null)
            root = BinaryNode(key, value)
        else {
            root!!.add(key, value)
        }
    }
}