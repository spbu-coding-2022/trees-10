package nodes

import nodes.BinaryNode

class AVLNode<K: Comparable<K>, V>(key: K, value: V){
    var height: Int = 1
    open var key : K = key
        protected set
    open var value : V? = value
        protected set

    open var right : AVLNode<K, V>? = null
    open var left : AVLNode<K, V>? = null
}