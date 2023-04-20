package trees

import exceptions.NodeNotFound
import nodes.BinaryNode

open class BinaryTree<K: Comparable<K>, V> : AbstractTree<K, V, BinaryNode<K,V>>() {
    override fun search(key : K) : BinaryNode<K,V>? = root?.search(key)
        ?: throw NodeNotFound()
    override fun remove(key: K)  {
        root = root?.remove(this.root, key)
            ?: throw NodeNotFound()
    }
    override fun add(key : K, value : V?) {
        if (root == null)
            root = BinaryNode(key, value)
        else
            root?.add(key, value)
    }
}