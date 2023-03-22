package binaryNode

class binaryNode<T: Comparable<T>, NodeType>(key : T, value : NodeType) : Comparable<binaryNode<T, NodeType>> {

    var key : T
    var value : NodeType

    private var right : binaryNode<T, NodeType>? = null
    private var left : binaryNode<T, NodeType>? = null

    init {
        key.also { this.key = it }
        value.also { this.value = it }
    }
    override fun compareTo(other: binaryNode<T, NodeType>): Int = this.key.compareTo(other.key)
}