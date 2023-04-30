package databases

interface IBase<TreeType, K> {
    fun saveTree(tree: TreeType)
    fun loadTree() : TreeType
    fun getCoordinate(key: K)
    fun setCoordinate(key: K, x: Double, y: Double)
}