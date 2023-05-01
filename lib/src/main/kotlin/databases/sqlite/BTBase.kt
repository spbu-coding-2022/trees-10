package databases.sqlite

import databases.IBase
import exceptions.NullNodeException
import nodes.BinaryNode
import trees.BinaryTree
import java.awt.Point
import java.io.Closeable
import java.sql.DriverManager
import java.sql.SQLException


class BTBase<V>(
    dbPath: String,
    private val serializeValue: (value: V?) -> String = { value -> value.toString() },
    private val deserializeValue: (strValue: String) -> V
) : IBase<BinaryTree<Int, V>, Int>, Closeable {
    object DbConstants {
        const val DB_NAME = "BinaryNodes"
    }

    private val connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val createBaseStatement by lazy { connection.prepareStatement("CREATE TABLE if not exists ${DbConstants.DB_NAME} (key int, value varchar(255), x double, y double);") }
    private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO ${DbConstants.DB_NAME} (key, value, x, y) VALUES (?, ?, ?, ?);") }
    private val setPointStatement by lazy { connection.prepareStatement("UPDATE ${DbConstants.DB_NAME} SET x=?, y=? WHERE key=?;") }
    private val getPointStatement by lazy { connection.prepareStatement("SELECT x, y FROM ${DbConstants.DB_NAME} WHERE key=?;") }
    private val getNodesStatement by lazy { connection.prepareStatement("SELECT key, value, x, y, value FROM ${DbConstants.DB_NAME}") }
    private val dropDatabaseStatement by lazy { connection.prepareStatement("DELETE FROM ${DbConstants.DB_NAME};") }

    init {
        createBaseStatement.execute()
    }

    override fun saveTree(tree: BinaryTree<Int, V>) {
        clear()
        if (tree.root == null)
            return
        tree.toList().forEach {
            addNode(it)
        }
    }

    override fun loadTree(): BinaryTree<Int, V> {
        val tree = BinaryTree<Int, V>()

        val stateRes = getNodesStatement.executeQuery()

        while (stateRes.next())
            tree.add(stateRes.getInt(1), deserializeValue(stateRes.getString(2)))
        return tree
    }

    override fun setPoint(key: Int, p: Point) {
        setPointStatement.setInt(1, p.x)
        setPointStatement.setInt(2, p.y)
        setPointStatement.setInt(3, key)

        setPointStatement.execute()
    }

    override fun getPoint(key: Int): Point {
        getPointStatement.setInt(1, key)

        val stateRes = getPointStatement.executeQuery()

        val p = Point(-1, -1)

        while (stateRes.next()) {
            p.x = stateRes.getInt(1)
            p.y = stateRes.getInt(2)
        }

        return p
    }

    private fun addNode(node: BinaryNode<Int, V>, p: Point = Point(0, 0)) {
        addNodeStatement.setInt(1, node.key)
        addNodeStatement.setString(2, serializeValue(node.value))
        addNodeStatement.setInt(3, p.x)
        addNodeStatement.setInt(4, p.y)

        addNodeStatement.execute()
    }

    private fun clear() {
        dropDatabaseStatement.execute()
    }

    override fun close() {
        createBaseStatement.close()
        addNodeStatement.close()
        getNodesStatement.close()
        dropDatabaseStatement.close()

        connection.close()
    }

    private fun BinaryTree<Int, V>.toList(): MutableList<BinaryNode<Int, V>> =
        this.root?.toList(mutableListOf()) ?: throw NullNodeException()

    private fun BinaryNode<Int, V>.toList(list: MutableList<BinaryNode<Int, V>>): MutableList<BinaryNode<Int, V>> {
        var myList: MutableList<BinaryNode<Int, V>> = list

        myList.add(this)
        this.left?.let {
            myList = it.toList(myList)
        }
        this.right?.let {
            myList = it.toList(myList)
        }

        return myList
    }

}