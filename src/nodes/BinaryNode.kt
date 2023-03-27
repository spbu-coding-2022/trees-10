class BinaryNode<T: Comparable<T>, NodeType>(key : T, value : NodeType) : Comparable<BinaryNode<T, NodeType>> {

    var key : T private set
    var value : NodeType private set

    private var right : BinaryNode<T, NodeType>? = null
    private var left : BinaryNode<T, NodeType>? = null

    init {
        key.also { this.key = it }
        value.also { this.value = it }
    }

    fun search(key: T) : NodeType? =
        when (this.compareTo(key)) {
            1 -> this.right?.search(key)
            0 -> this.value
            -1 -> this.left?.search(key)
            else -> null
        }

    override fun toString(): String {
        return "<$key, $value>"
    }
    override fun compareTo(other: BinaryNode<T, NodeType>): Int = this.key.compareTo(other.key)
    fun compareTo(key: T): Int = this.key.compareTo(key)
}