package trees

import nodes.AbstractNode

abstract class AbstractTree<K : Comparable<K>, V, node : AbstractNode<K, V, node>> {
    var root : node? = null
        internal set
    abstract fun search(key : K) : node?
    abstract fun add(key : K, value : V? = null)
    abstract fun remove(key : K)
}