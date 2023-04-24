package databases.binTree

import trees.BinaryTree
import java.io.Closeable
import java.sql.DriverManager
import java.sql.SQLException


class BinaryBase <V>(
    dbPath: String,
    private val serializeValue: (value: V?) -> String,
    private val deserializeValue: (strValue: String) -> V
) : Closeable {

    private val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val createBaseStatement by lazy { connection.prepareStatement("CREATE TABLE if not exists BinaryNodes (key int, value varchar(255), x double, y double);") }
    private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO BinaryNodes (key, value, x, y) VALUES (?, ?, ?, ?);") }
    private val getNodesStatement by lazy { connection.prepareStatement("SELECT key, value, x, y, value FROM BinaryNodes") }
    fun open() = createBaseStatement.execute()
    fun saveTree(tree: WrappedBinTree<Int, V>) {
        for (node in tree.getWrappedNodesArray())
            addNode(node)
    }

    fun saveTree(tree: BinaryTree<Int, V>) = saveTree(WrappedBinTree(tree))
    fun getWrappedBinTree(): WrappedBinTree<Int, V> {
        val tree = WrappedBinTree<Int, V>()

        val stateRes = getNodesStatement.executeQuery()

        while (!stateRes.isClosed && stateRes.next()) {
            tree.add(stateRes.getInt(1), deserializeValue(stateRes.getString(2)), stateRes.getDouble(3), stateRes.getDouble(4))
        }

        return tree
    }

    fun getBinaryTree(): BinaryTree<Int, V> {
        val tree = BinaryTree<Int, V>()

        val stateRes = getNodesStatement.executeQuery()

        while (!stateRes.isClosed && stateRes.next()) {
            tree.add(stateRes.getInt(1), deserializeValue(stateRes.getString(2)))
        }

        return tree
    }

    private fun addNode(node: WrappedBinNode<Int, V>) {

        addNodeStatement.setInt(1, node.key)
        addNodeStatement.setString(2, serializeValue(node.value) ?: "empty")
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