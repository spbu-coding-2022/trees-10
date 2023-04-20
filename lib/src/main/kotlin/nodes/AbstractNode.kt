package nodes

abstract class AbstractNode<K : Comparable<K>, V, node : AbstractNode<K, V, node>>() {
    abstract var key : K
        protected set
    abstract var value : V?
        protected set

    abstract var right : node?
    abstract var left : node?
}