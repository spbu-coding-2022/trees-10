package nodes

class RBNode<K : Comparable<K>, V>(override var key: K, override var value: V?) :
    AbstractNode<K, V, RBNode<K, V>>() {

    override var right: RBNode<K, V>? = null
    override var left: RBNode<K, V>? = null

    var parent: RBNode<K, V>? = null
    var color: Color = Color.BLACK

    fun flipColor() {
        color = when (color) {
            Color.RED -> Color.BLACK
            Color.BLACK -> Color.RED
        }
    }
}

enum class Color { RED, BLACK }
