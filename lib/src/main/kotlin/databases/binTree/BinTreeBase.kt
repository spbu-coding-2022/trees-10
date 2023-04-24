package databases.binTree

import trees.BinaryTree
import java.io.Closeable
import java.sql.DriverManager
import java.sql.SQLException


class BinTreeBase<V>(
    dbPath: String,
    private val serializeValue: (value: V?) -> String = { value -> value.toString() },
    private val deserializeValue: (strValue: String) -> V
) : Closeable {
    object DbConstants {
        const val DB_NAME = "BinaryNodes"
    }

    private val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val createBaseStatement by lazy { connection.prepareStatement("CREATE TABLE if not exists ${DbConstants.DB_NAME} (key int, value varchar(255), x double, y double);") }
    private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO ${DbConstants.DB_NAME} (key, value, x, y) VALUES (?, ?, ?, ?);") }
    private val getNodesStatement by lazy { connection.prepareStatement("SELECT key, value, x, y, value FROM ${DbConstants.DB_NAME}") }
    private val dropDatabaseStatement by lazy { connection.prepareStatement("DELETE FROM ${DbConstants.DB_NAME};") }


    init {
        createBaseStatement.execute()
    }

    fun saveTree(tree: WrappedBinTree<Int, V>) {
        for (node in tree.getWrappedNodesArray())
            addNode(node)
    }

    fun clear() {
        dropDatabaseStatement.execute()
    }
    fun getWrappedBinTree(): WrappedBinTree<Int, V> {
        val tree = WrappedBinTree<Int, V>()

        val stateRes = getNodesStatement.executeQuery()

        while (!stateRes.isClosed && stateRes.next()) {
            tree.add(
                stateRes.getInt(1),
                deserializeValue(stateRes.getString(2)),
                stateRes.getDouble(3),
                stateRes.getDouble(4)
            )
        }

        return tree
    }

    private fun addNode(node: WrappedBinNode<Int, V>) {

        addNodeStatement.setInt(1, node.key)
        addNodeStatement.setString(2, serializeValue(node.value))
        addNodeStatement.setDouble(3, node.x)
        addNodeStatement.setDouble(4, node.y)

        addNodeStatement.execute()
    }

    override fun close() {
        createBaseStatement.close()
        addNodeStatement.close()

        connection.close()
    }

}