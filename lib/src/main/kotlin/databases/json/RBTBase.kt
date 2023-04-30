package databases.json

import databases.IBase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nodes.RBNode
import trees.RBTree
import java.io.File

class RBTBase<V>(
    private val dbPath: String,
    private val serializeValue: (value: V?) -> String = { value -> value.toString() },
    private val deserializeValue: (strValue: String) -> V
) : IBase<RBTree<Int, V>> {

    private var file: File = File(dbPath)
    override fun saveTree(tree: RBTree<Int, V>) {
        file.printWriter().use { out ->
            out.write(tree.convertToJson())
        }
    }

    override fun loadTree(): RBTree<Int, V> {
        TODO("Not yet implemented")
    }

    override fun close() {
    }

    private fun RBTree<Int, V>.convertToJson() = Json.encodeToString(JsonRBTree(root?.convertToJson()))
    private fun RBNode<Int, V>.convertToJson(x: Double = 0.0, y: Double = 0.0): JsonRBNode =
        JsonRBNode(
            serializeValue(this.value),
            this.key,
            this.color,
            x,
            y,
            this.left?.convertToJson(),
            this.right?.convertToJson()
        )


}