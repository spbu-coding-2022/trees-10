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
        when (key.compareTo(this.key)) {
            1 -> this.right?.search(key)
            0 -> this.value
            -1 -> this.left?.search(key)
            else -> null
    }

    fun delete(key: T): Nothing = TODO("delete NODE fun")

    @Throws(Exception::class)
    fun insert(key : T, value : NodeType) {
        val compare = key.compareTo(this.key)

        if (compare == 1) {
            if (right == null)
                right = BinaryNode(key, value)
            else
                right!!.insert(key, value)
        } else if (compare == 0) {
            throw Exception("Keys can't be equal")
        } else {
            if (left == null)
                left = BinaryNode(key, value)
            else
                left!!.insert(key, value)
        }
    }

    override fun toString(): String {
        return "<$key, $value>"
    }
    override fun compareTo(other: BinaryNode<T, NodeType>): Int = this.key.compareTo(other.key)
    fun compareTo(key: T): Int = this.key.compareTo(key)
}