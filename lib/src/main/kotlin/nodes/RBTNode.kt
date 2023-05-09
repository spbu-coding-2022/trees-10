package nodes

class RBNode<K : Comparable<K>, V>(key: K, value: V?) :
    AbstractNode<K, V, RBNode<K, V>>(key, value) {

    var parent: RBNode<K, V>? = null
    override var color: Color = Color.BLACK
}
