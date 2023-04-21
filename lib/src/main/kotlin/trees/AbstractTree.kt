package trees

import nodes.AbstractNode

abstract class AbstractTree<K : Comparable<K>, V, NodeType : AbstractNode<K, V, NodeType>> {
    protected abstract var root : NodeType?
    abstract fun search(key : K) : NodeType?
    abstract fun add(key : K, value : V? = null)
    abstract fun remove(key : K)
}