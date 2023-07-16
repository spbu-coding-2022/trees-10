package nodes

abstract class AbstractNode<K : Comparable<K>, V, node : AbstractNode<K, V, node>>(key: K, value: V?) :
    Comparable<AbstractNode<K, V, node>> {
    var key: K = key
        protected set
    var value: V? = value
        internal set
    var right: node? = null
        internal set
    var left: node? = null
        internal set
    abstract var color: Color
    override fun compareTo(other: AbstractNode<K, V, node>): Int = this.key.compareTo(other.key)
}

enum class Color { RED, BLACK, YELLOW, DARK_GRAY, GRAY}