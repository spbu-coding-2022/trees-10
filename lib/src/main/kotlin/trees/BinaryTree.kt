package trees

import nodes.BinaryNode

open class BinaryTree<T: Comparable<T>, NodeType> : ITree<T, NodeType> {
    protected open var root : BinaryNode<T, NodeType>? = null
    override fun search(key : T) : NodeType? = root?.search(key)
    override fun remove(key: T)  {
        root = root?.remove(this.root, key)
    }
    override fun add(key : T, value : NodeType?) {
        if (root == null)
            root = BinaryNode(key, value)
        else {
            root!!.add(key, value)
        }
    }

}