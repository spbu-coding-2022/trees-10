package nodes

import nodes.BinaryNode

class AVLNode<K: Comparable<K>, V>(override var key: K, override var value: V?) : AbstractNode<K, V, AVLNode<K, V>>(){
    override var right : AVLNode<K, V>? = null
    override var left : AVLNode<K, V>? = null

    var height: Int = 1
}