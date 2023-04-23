package databases.binTree

import trees.BinaryTree
import java.io.Closeable
import java.sql.DriverManager
import java.sql.SQLException

class BinaryBase(dbPath: String) : Closeable {

    private val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val createBaseStatement by lazy { connection.prepareStatement("CREATE TABLE if not exists BinaryNodes (key int, value varchar(255), x double, y double);") }
    private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO BinaryNodes (key, value, x, y) VALUES (?, ?, ?, ?);") }
    private val getNodesStatement by lazy { connection.prepareStatement("SELECT key, value, x, y, value FROM BinaryNodes") }
    fun open() = createBaseStatement.execute()

    fun saveTree(tree: WrappedBinTree<Int, String>) {
        for (node in tree.getWrappedNodesArray())
            addNode(node)
    }

    fun saveTree(tree: BinaryTree<Int, String>) = saveTree(WrappedBinTree(tree))
    fun getWrappedBinTree(): WrappedBinTree<Int, String> {
        val tree = WrappedBinTree<Int, String>()

        val stateRes = getNodesStatement.executeQuery()

        while (!stateRes.isClosed && stateRes.next()) {
            tree.add(stateRes.getInt(1), stateRes.getString(2), stateRes.getDouble(3), stateRes.getDouble(4))
        }

        return tree
    }

    fun getBinaryTree(): BinaryTree<Int, String> {
        val tree = BinaryTree<Int, String>()

        val stateRes = getNodesStatement.executeQuery()

        while (!stateRes.isClosed && stateRes.next()) {
            tree.add(stateRes.getInt(1), stateRes.getString(2))
        }

        return tree
    }

    private fun addNode(node: WrappedBinNode<Int, String>) {
        addNodeStatement.setInt(1, node.key)
        addNodeStatement.setString(2, node.value ?: "empty")
        addNodeStatement.setDouble(3, node.x)
        addNodeStatement.setDouble(4, node.y)

        addNodeStatement.execute()
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

        connection.close()
    }

}