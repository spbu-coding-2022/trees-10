package nodes

abstract class AbstractNode<K : Comparable<K>, V, node : AbstractNode<K, V, node>>() {
    lateinit var key : K
        protected set
    var value : V? = null
     protected set

    var right : node? = null
    var left : node? = null
}