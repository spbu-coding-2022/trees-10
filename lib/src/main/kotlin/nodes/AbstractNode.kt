package nodes

abstract class AbstractNode<K : Comparable<K>, V, node : AbstractNode<K, V, node>>(key: K, value: V?) :
    Comparable<AbstractNode<K, V, node>> {
    var key: K = key
        internal set

    var value: V? = value
        internal set

    var right: node? = null
    var left: node? = null

    override fun compareTo(other: AbstractNode<K, V, node>): Int = this.key.compareTo(other.key)
}