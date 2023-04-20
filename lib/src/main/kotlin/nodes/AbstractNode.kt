package nodes

abstract class AbstractNode<K : Comparable<K>, V, node : AbstractNode<K, V, node>>(key: K, value : V?) {
    var key : K = key
        protected set
    var value : V? = value
     protected set

    var right : node? = null
    var left : node? = null
}