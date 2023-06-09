package nodes

class AVLNode<K: Comparable<K>, V>(key: K, value: V?) : AbstractNode<K, V, AVLNode<K, V>>(key, value){
    var height: Int = 1
    override var color: Color = Color.GRAY
}
