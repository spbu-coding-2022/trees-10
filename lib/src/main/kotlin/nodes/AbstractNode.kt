package nodes

abstract class AbstractNode<K : Comparable<K>, V, NodeType : AbstractNode<K, V, NodeType>>() {
    abstract var key : K
        protected set
    abstract var value : V?
        internal set

    abstract var right : NodeType?
    abstract var left : NodeType?
}