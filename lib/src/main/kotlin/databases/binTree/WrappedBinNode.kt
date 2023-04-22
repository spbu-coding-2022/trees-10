package databases.binTree

import nodes.AbstractNode
import nodes.BinaryNode

class WrappedBinNode<K : Comparable<K>, V>(key: K, value: V?, var x: Double = 0.0, var y: Double = 0.0) :
    AbstractNode<K, V, BinaryNode<K, V>>(key, value) {

}