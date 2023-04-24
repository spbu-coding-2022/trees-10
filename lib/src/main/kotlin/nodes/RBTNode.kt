package RBnode

import nodes.AbstractNode

class RBNode<K : Comparable<K>, V>(key: K, value: V?) :
    AbstractNode<K, V, RBNode<K, V>>(key, value) {

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
