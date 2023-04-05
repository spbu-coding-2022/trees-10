class BinaryNode<T: Comparable<T>, NodeType>(key : T, value : NodeType, parent : BinaryNode<T, NodeType>?) : Comparable<BinaryNode<T, NodeType>> {

    var key : T private set
    var value : NodeType private set

//    private var parent : BinaryNode<T, NodeType>? = null

    private var right : BinaryNode<T, NodeType>? = null
    private var left : BinaryNode<T, NodeType>? = null

    var hasChildren: Boolean
        get() = !(this.right == null && this.left == null)
        private set

    init {
        hasChildren = false
        key.also { this.key = it }
        value.also { this.value = it }
//        parent?.also { this.parent = it }
    }

    fun search(key: T) : NodeType? =
        when (key.compareTo(this.key)) {
            1 -> this.right?.search(key)
            0 -> this.value
            -1 -> this.left?.search(key)
            else -> null
    }

    private fun nodeRemove(node : BinaryNode<T, NodeType>?) : BinaryNode<T, NodeType>? {
            if (!node!!.hasChildren)
                return null

                if (node!!.left != null || node!!.right == null) {
                    if (node!!.right == null && node!!.left != null)
                        return node!!.left
                    else {
                        TODO("остаточное условие")
                    }
                } else
                    return node!!.right

    }
    fun delete(key: T) {
        if     (right?.key == key)
            right = nodeRemove(right)
       else if (left?.key == key) {
           left = nodeRemove(left)
       } else {
           if (key.compareTo(this.key) == -1)
               left?.delete(key)
            else
                right?.delete(key)
       }

    }
    @Throws(Exception::class)
    fun insert(key : T, value : NodeType) {
        val compare = key.compareTo(this.key)

        if (compare == 1) {
            if (right == null)
                right = BinaryNode(key, value, this)
            else
                right!!.insert(key, value)
        } else if (compare == 0) {
            throw Exception("Keys can't be equal")
        } else {
            if (left == null)
                left = BinaryNode(key, value, this)
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