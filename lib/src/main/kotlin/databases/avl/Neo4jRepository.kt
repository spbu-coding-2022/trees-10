package databases.avl

import nodes.AVLNode
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory
import trees.AVLTree

@NodeEntity
class AVLNodeEntity<K, V>(
    @Property
    val key: K,

    @Property
    var value: V?,

    @Property
    var x: Double = 0.0,

    @Property
    var y: Double = 0.0,

    @Relationship(type = "LEFT")
    var left: AVLNodeEntity<K, V>? = null,

    @Relationship(type = "RIGHT")
    var right: AVLNodeEntity<K, V>? = null,
){
    @Id
    @GeneratedValue
    var id: Long? = null
}

@NodeEntity
class AVLTreeEntity<K, V>(
    @Property
    var name: String,

    @Relationship(type = "ROOT")
    var root: AVLNodeEntity<K, V>? = null,
){
    @Id
    @GeneratedValue
    var id: Long? = null
}

class Neo4jRepository<K: Comparable<K>, V>(uri: String, username: String, password: String) {
    private val configuration: Configuration = Configuration.Builder()
        .uri(uri)
        .credentials(username, password)
        .build();

    private val sessionFactory = SessionFactory(configuration, "databases.avl")
    private val session = sessionFactory.openSession()

    private fun AVLTree<K, V>.toEntity(name: String) : AVLTreeEntity<K, V> {
        return AVLTreeEntity<K, V>(
            name,
            root?.toEntity()
        )
    }

    private fun AVLNode<K, V>.toEntity() : AVLNodeEntity<K, V>{
        return AVLNodeEntity<K, V>(
            key,
            value,
            0.0, //Нужен класс который смог бы хранить эти координаты
            0.0,
            left?.toEntity(),
            right?.toEntity()
        )
    }

    fun saveTree(tree: AVLTree<K, V>, name: String) {
        deleteTree(name)
        session.save(tree.toEntity(name))
    }

    fun loadTree() {
        TODO("Not yet implemented")
    }

    fun deleteTree(name: String) {
        session.query(
            "MATCH (t: AVLTreeEntity {name: \$name})-[*0..]-(x)" +
                    "DETACH DELETE x",
            mapOf("name" to name)
        )
    }
}