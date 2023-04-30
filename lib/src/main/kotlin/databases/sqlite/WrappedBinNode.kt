package databases.sqlite
data class WrappedBinNode<K : Comparable<K>, V>(var key: K, var value: V?, var x: Double = 0.0, var y: Double = 0.0)