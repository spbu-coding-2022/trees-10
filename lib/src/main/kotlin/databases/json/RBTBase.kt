package databases.json

import databases.IBase
import exceptions.NodeNotFoundException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nodes.RBNode
import trees.RBTree
import java.awt.Point
import java.io.File

class RBTBase<K: Comparable<K>, V>(
    private val dbPath: String,
    private val serializeKey: (key: K) -> String = { value -> value.toString() },
    private val deserializeKey: (strKey: String) -> K,
    private val serializeValue: (value: V?) -> String = { value -> value.toString() },
    private val deserializeValue: (strValue: String) -> V
) : IBase<RBTree<K, V>, K> {

    private var file: File = File(dbPath)
    override fun saveTree(tree: RBTree<K, V>) {
        file.printWriter().use { out ->
            out.write(tree.convertToJson())
        }
    }

    override fun loadTree(): RBTree<K, V> = Json.decodeFromString<JsonRBTree>(file.readText()).convertToTree()

    override fun setPoint(key: K, p: Point) {
        val savedTree = Json.decodeFromString<JsonRBTree>(file.readText())

        if (savedTree.root?.changeCoordinate(key, p) != true)
            throw NodeNotFoundException()

        file.printWriter().use { out ->
            out.write(Json.encodeToString(savedTree))
        }

    }

    override fun getPoint(key: K): Point {
        return Json.decodeFromString<JsonRBTree>(file.readText()).root?.searchCoordinate(key)
            ?: throw NodeNotFoundException()
    }

    private fun JsonRBNode.searchCoordinate(key: K): Point? {
        if (this.key == key)
            return Point(this.x, this.y)
        else {
            this.left?.searchCoordinate(key)?.also {
                return Point(it.x, it.y)
            }
            this.right?.searchCoordinate(key)?.also {
                return Point(it.x, it.y)
            }
        }

        return null
    }

    private fun JsonRBNode.changeCoordinate(key: K, p: Point): Boolean {
        return if (this.key == key) {
            this.x = p.x
            this.y = p.y
            true
        } else {
            if (this.left?.changeCoordinate(key, p) != true)
                this.right?.changeCoordinate(key, p) == true
            else
                true
        }
    }

    private fun JsonRBTree.convertToTree(): RBTree<K, V> =
        RBTree<K, V>().also {
            it.root = this.root?.convertToNode()
        }

    private fun JsonRBNode.convertToNode(): RBNode<K, V> =
        RBNode(deserializeKey(this.key), deserializeValue(this.value)).also {
            it.color = this.color
            it.left = this.left?.convertToNode()
            it.right = this.right?.convertToNode()
        }

    private fun RBTree<K, V>.convertToJson() = Json.encodeToString(JsonRBTree(root?.convertToJson()))
    private fun RBNode<K, V>.convertToJson(x: Int = 0, y: Int = 0): JsonRBNode =
        JsonRBNode(
            serializeValue(this.value),
            serializeKey(this.key),
            this.color,
            x,
            y,
            this.left?.convertToJson(),
            this.right?.convertToJson()
        )


}