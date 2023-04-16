package trees

import nodes.BinaryNode

open class BinaryTree<K: Comparable<K>, V> : AbstractTree<K, V, BinaryNode<K,V>>() {
    override var root : BinaryNode<K, V>? = null
    override fun search(key : K) : BinaryNode<K,V>? = root?.search(key)
    override fun remove(key: K)  {
        root = root?.remove(this.root, key)
    }
    override fun add(key : K, value : V?) {
        if (root == null)
            root = BinaryNode(key, value)
        else
            root!!.add(key, value)
    }
}