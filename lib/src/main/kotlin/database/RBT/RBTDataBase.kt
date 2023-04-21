package database.RBT

import trees.RBTree
import java.io.*

class RBTreeSerializer {
    fun serialize(tree: RBTree<*, *>, filePath: String) {
        val fileOutputStream = FileOutputStream(filePath)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(tree)
        objectOutputStream.close()
        fileOutputStream.close()
    }

    fun deserialize(filePath: String): RBTree<*, *> {
        val fileInputStream = FileInputStream(filePath)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val tree = objectInputStream.readObject() as RBTree<*, *>
        objectInputStream.close()
        fileInputStream.close()
        return tree
    }
}
