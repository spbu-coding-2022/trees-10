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

    fun remove(root : BinaryNode<T, NodeType>?, key : T) : BinaryNode<T, NodeType>? {
        if (root == null)
            return null

        if (key == this.key) { // когда remove вызывается для удаляемой вершины
            if (this.right == null && this.left == null)
                return null
            // Простой случай - если есть только 1 потомок
            else if  (this.left == null)
                return this.right
            else if (this.right == null)
                return this.left
            // Есть оба поддерева
            else {
                // Находим минимальное дерево
                // Перенимаем его key и value
                // Удаляем минимальное дерево
                val minNode = findMin(this.right)
                this.key = minNode!!.key
                this.value = minNode.value
                this.right = right!!.remove(this.right, minNode.key)
                return this
            }

        } else {
            // Замещаем поддеревья на минимальные
            if (key < this.key)
                this.left = left?.remove(left, key)
            else
                this.right = right?.remove(right, key)
            return root
        }
    }

    private fun findMin(node: BinaryNode<T, NodeType>?): BinaryNode<T, NodeType>? = if (node?.left != null) findMin(node!!.left) else node

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