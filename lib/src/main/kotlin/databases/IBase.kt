package databases

import java.io.Closeable

interface IBase<TreeType>: Closeable {
    fun saveTree(tree: TreeType)
    fun loadTree() : TreeType
}