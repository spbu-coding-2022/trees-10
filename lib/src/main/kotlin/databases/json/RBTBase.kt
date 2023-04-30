package databases.json

import databases.IBase
import trees.RBTree

class RBTBase<V>(
    private val dbPath: String,
    private val serializeValue: (value: V?) -> String = { value -> value.toString() },
    private val deserializeValue: (strValue: String) -> V
): IBase<RBTree<Int, String>> {

    val savedTree: RBTree<Int, String> = RBTree()
    override fun saveTree(tree: RBTree<Int, String>) {
        TODO("Not yet implemented")
    }

    override fun loadTree(): RBTree<Int, String> {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

}