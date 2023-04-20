package trees

import exceptions.NodeNotFoundException
import nodes.BinaryNode

class BinaryTree<K: Comparable<K>, V> : AbstractTree<K, V, BinaryNode<K,V>>() {
    override fun search(key : K) : BinaryNode<K,V> = root?.search(key)
        ?: throw NodeNotFoundException()
    override fun remove(key: K)  {
        if (root == null)
            throw NodeNotFoundException()
        root = root?.remove(this.root, key)
    }
    override fun add(key : K, value : V?) {
        if (root == null)
            root = BinaryNode(key, value)
        else
            root?.add(key, value)
    }
}