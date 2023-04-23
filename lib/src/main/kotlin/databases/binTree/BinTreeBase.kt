package databases.binTree

import nodes.BinaryNode
import trees.BinaryTree
import java.io.Closeable
import java.sql.DriverManager
import java.sql.SQLException
class BinaryBase (dbPath: String): Closeable {

    private val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val createBaseStatement by lazy {connection.prepareStatement("CREATE TABLE if not exists BinaryNodes (key int, value varchar(255), x double, y double);")}
    private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO BinaryNodes (key, value, x, y) VALUES (?, ?, ?, ?);") }
    private val getNodeByKey by lazy { connection.prepareStatement("SELECT x, y, value FROM BinaryNodes WHERE BinaryNodes.key = ?;") }
    private val removeNodeByKey by lazy { connection.prepareStatement("DELETE FROM BinaryNodes WHERE key=?;") }
    fun open() = createBaseStatement.execute()

    fun saveTree(tree: WrappedBinTree<Int, String>) {
        for (node in tree.getWrappedNodesArray())
            addNode(node)
    }
    fun saveTree(tree: BinaryTree<Int, String>) = saveTree(WrappedBinTree(tree))
    fun removeNode(key : Int) {
        removeNodeByKey.setInt(1, key)
        removeNodeByKey.execute()
    }
    private fun addNode(node : WrappedBinNode<Int, String>) {
        if (search(node.key) != null)
            return

        addNodeStatement.setInt(1, node.key)
        addNodeStatement.setString(2, node.value ?: "empty")
        addNodeStatement.setDouble(3, node.x)
        addNodeStatement.setDouble(4, node.y)

        addNodeStatement.execute()
    }
    fun search(key: Int) : WrappedBinNode<Int, String>? {
        getNodeByKey.setInt(1, key)
        val res = getNodeByKey.executeQuery()
        if (res.isClosed)
            return null

        return WrappedBinNode(key, res.getString("value"), res.getDouble("x"), res.getDouble("y"))
    }
    constructor(dbPath: String, tree: WrappedBinTree<Int, String>) : this(dbPath) {
        saveTree(tree)
    }
    constructor(dbPath: String, tree: BinaryTree<Int, String>) : this(dbPath) {
        saveTree(tree)
    }
    override fun close() {
        createBaseStatement.close()
        addNodeStatement.close()
        getNodeByKey.close()
        removeNodeByKey.close()

        connection.close()
    }

}