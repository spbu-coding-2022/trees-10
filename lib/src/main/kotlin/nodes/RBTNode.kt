package nodes

class RBNode<K : Comparable<K>, V>(key: K, value: V?) :
    AbstractNode<K, V, RBNode<K, V>>(key, value) {

    var parent: RBNode<K, V>? = null
    var color: Color = Color.BLACK
}

enum class Color { RED, BLACK }
