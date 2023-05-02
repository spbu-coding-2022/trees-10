package databases.avl

import nodes.AVLNode
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.cypher.ComparisonOperator
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.Filters
import org.neo4j.ogm.session.SessionFactory
import trees.AVLTree
import java.awt.Point

@NodeEntity
class AVLNodeEntity(
    @Property
    val key: String,

    @Property
    var value: String?,

    @Property
    var height: Int,

    @Property
    var x: Int = 0,

    @Property
    var y: Int = 0,

    @Relationship(type = "LEFT")
    var left: AVLNodeEntity? = null,

    @Relationship(type = "RIGHT")
    var right: AVLNodeEntity? = null,
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}

@NodeEntity
class AVLTreeEntity(
    @Property
    var name: String,

    @Relationship(type = "ROOT")
    var root: AVLNodeEntity? = null,
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}

class Neo4jRepository<K : Comparable<K>, V>(
    uri: String,
    username: String,
    password: String,
    private val serializeKey: (key: K) -> String = { value -> value.toString() },
    private val deserializeKey: (strKey: String) -> K,
    private val serializeValue: (value: V?) -> String = { value -> value.toString() },
    private val deserializeValue: (strValue: String?) -> V?
) {
    private val configuration: Configuration = Configuration.Builder()
        .uri(uri)
        .credentials(username, password)
        .build();

    private val sessionFactory = SessionFactory(configuration, "databases.avl")
    private val session = sessionFactory.openSession()

    private fun AVLTree<K, V>.toEntity(name: String = "Default"): AVLTreeEntity {
        return AVLTreeEntity(
            name,
            root?.toEntity()
        )
    }

    private fun AVLNode<K, V>.toEntity(x: Int = 0, y: Int = 0): AVLNodeEntity {
        return AVLNodeEntity(
            serializeKey(key),
            serializeValue(value),
            height,
            x,
            y,
            left?.toEntity(),
            right?.toEntity()
        )
    }

    private fun AVLTreeEntity.toTree(): AVLTree<K, V> {
        return AVLTree<K, V>().also {
            it.root = this.root?.toNode()
        }
    }

    private fun AVLNodeEntity.toNode(): AVLNode<K, V> {
        return AVLNode(deserializeKey(key), deserializeValue(value)).also {
            it.left = this.left?.toNode()
            it.right = this.right?.toNode()
            it.height = this.height
        }
    }

    private fun findTree(name: String = "Default") = session.loadAll(
        AVLTreeEntity::class.java,
        Filter("name", ComparisonOperator.EQUALS, name), -1
    )

    fun saveTree(tree: AVLTree<K, V>, name: String = "Default") {
        deleteTree(name)
        session.save(tree.toEntity(name))
    }

    fun loadTree(name: String = "Default"): AVLTree<K, V>? {
        val treeEntity = findTree(name).singleOrNull()

        return treeEntity?.toTree()
    }

    fun deleteTree(name: String = "Default") {
        session.query(
    "MATCH (t: AVLTreeEntity {name: \$name})-[*0..]-(x)" +
            "DETACH DELETE x",
            mapOf("name" to name)
        )
    }

    fun getPoint(key: K, name: String = "Default"): Point {
        val node = session.queryForObject(AVLNodeEntity::class.java,
        "MATCH(t:AVLTreeEntity {name: \$name})" +
            "MATCH(n:AVLNodeEntity {key: \$key})<-[*0..]-(t)" +
            "return n",
            mapOf("name" to name, "key" to serializeKey(key))
        )

        return Point(node.x, node.y)
    }

    fun setPoint(key: K, point: Point, name: String = "Default") {
        session.query(
        "MATCH(t:AVLTreeEntity {name: \$name})" +
            "MATCH(n:AVLNodeEntity {key: \$key})<-[*0..]-(t)" +
            "SET n.x = \$x, n.y = \$y",
            mapOf("name" to name, "key" to serializeKey(key), "x" to point.x, "y" to point.y)
        )
    }

    /*fun setPoint(key: K, p: Point) {
        val savedTree = Json.decodeFromString<JsonRBTree>(file.readText())

        if (savedTree.root?.changeCoordinate(key, p) != true)
            throw NodeNotFoundException()

        file.printWriter().use { out ->
            out.write(Json.encodeToString(savedTree))
        }

    }

    fun getPoint(key: K): Point {
        return Json.decodeFromString<JsonRBTree>(file.readText()).root?.searchCoordinate(key)
            ?: throw NodeNotFoundException()
    }*/
}