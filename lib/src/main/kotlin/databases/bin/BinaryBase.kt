package databases.bin

import nodes.BinaryNode
import java.io.Closeable
import java.sql.DriverManager
import java.sql.SQLException

data class Node<NodeType>(val binNode: NodeType, val x: Int, val y : Int)
class BinaryBase (path: String): Closeable {

    private val connection = DriverManager.getConnection("jdbc:sqlite:$path")
        ?: throw SQLException("Cannot connect to database")
    private val createBaseStatement by lazy {connection.prepareStatement("CREATE TABLE if not exists BinaryNodes (x int, y int, key int, value varchar(255));")}
    private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO BinaryNodes (x, y, key, value) VALUES (?, ?, ?, ?);") }
    private val getNodeByKey by lazy { connection.prepareStatement("SELECT x, y, value FROM BinaryNodes WHERE BinaryNodes.key = ?;") }
    private val removeNodeByKey by lazy { connection.prepareStatement("DELETE FROM BinaryNodes WHERE key=?;") }
    fun open() = createBaseStatement.execute()

    fun removeNode(key : Int) {
        removeNodeByKey.setInt(1, key)
        removeNodeByKey.execute()
    }
    fun addNode(node : Node<BinaryNode<Int, String>>) {
        if (search(node.binNode.key) != null)
            return

        addNodeStatement.setInt(1, node.x)
        addNodeStatement.setInt(2, node.y)
        addNodeStatement.setInt(3, node.binNode.key)
        addNodeStatement.setString(4, node.binNode.value ?: "empty")

        addNodeStatement.execute()
    }
    fun search(key: Int) : Node<BinaryNode<Int, String>>? {
        getNodeByKey.setInt(1, key)
        val res = getNodeByKey.executeQuery()
        if (res.isClosed)
            return null

        return Node(BinaryNode(key, res.getString("value")), res.getInt("x"), res.getInt("y"))
    }
    override fun close() {
        createBaseStatement.close()
        addNodeStatement.close()
        getNodeByKey.close()
        removeNodeByKey.close()

        connection.close()
    }

}