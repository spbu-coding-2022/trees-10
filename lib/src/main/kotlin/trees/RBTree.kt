package trees

import exceptions.*
import nodes.Color
import nodes.RBNode


class RBTree<K : Comparable<K>, V> : AbstractTree<K, V, RBNode<K, V>>() {
    override fun search(key: K): RBNode<K, V> {
        var node = root
        while (node != null) {
            node = when {
                key < node.key -> node.left
                key > node.key -> node.right
                else -> return node                     //когда нода найдена - возвращаем ее
            }
        }
        throw NodeNotFoundException()
    }

    override fun add(key: K, value: V?) {
        if (contains(key)) {                // проверяем, существует ли нода с таким ключом, если да - выводим сообщение
            throw NodeAlreadyExistsException()
        }
        val newNode = RBNode(key, value)
        if (root == null) {                 // если нет корня - записываем в корень
            root = newNode
            root?.color = Color.BLACK
            return
        }

        var node = root
        var parent: RBNode<K, V>? = null
        while (node != null) {                      //спускаемся вниз по дереву поэлементно до null-листа
            parent = node
            node = if (key < node.key) node.left else node.right
        }

        newNode.color = Color.RED
        newNode.parent = parent

        if (key < (parent?.key ?: throw NullNodeException())) { //заменяем нужный null-лист элементом
            parent.left = newNode
        } else {
            parent.right = newNode
        }

        fixInsert(newNode)      //проверяем на баланс
    }

    override fun remove(key: K) {
        val node = search(key)            //проверяем на существование ноду
        removeNode(node)
    }

    private fun removeNode(node: RBNode<K, V>) {
        var y: RBNode<K, V>
        var replaceNode: RBNode<K, V>
        var yOriginalColor = node.color
        if (node.right != null || node.left != null) {
            if (node.left == null) {
                replaceNode = node.right ?: throw NullNodeException()
                transplant(node, node.right)
            } else if (node.right == null) {
                replaceNode = node.left ?: throw NullNodeException()
                transplant(node, node.left)
            } else {
                y = node.right ?: throw NullNodeException()
                while (y.left != null) {
                    y = y.left!!
                }
                yOriginalColor = y.color
                replaceNode = if (y.right == null) {
                    y
                } else {
                    y.right ?: throw NullNodeException()
                }
                if (replaceNode == y) {
                    replaceNode.parent = y.parent
                } else {
                    if (y.parent == node) {
                        replaceNode.parent = y
                    } else {
                        transplant(y, y.right)
                        y.right = node.right
                        y.right?.parent = y
                        replaceNode.parent = y.parent
                    }
                }
                transplant(node, y)
                y.left = node.left
                y.left?.parent = y
                y.color = node.color
            }
            if (yOriginalColor == Color.BLACK) {
                fixDelete(replaceNode)
            }
        } else {
            if (node.parent == null) {
                root = null
            } else {
                if (node.parent?.left == node) {
                    node.parent?.left = null
                } else {
                    node.parent?.right = null
                }
            }
        }
    }

    private fun fixInsert(node: RBNode<K, V>) {
        var newNode = node
        while (newNode.parent?.color == Color.RED) {    // балансируем, пока идут две красные ноды подряд
            if (newNode.parent == newNode.parent?.parent?.left) {      //если отец - левый сын дедушки
                val uncle = newNode.parent?.parent?.right
                if (uncle?.color == Color.RED) {
                    newNode.parent?.color = Color.BLACK
                    uncle.color = Color.BLACK
                    newNode.parent?.parent?.color = Color.RED
                    newNode = newNode.parent?.parent ?: throw NullNodeException()
                } else {                                                // если дядя черный или его нет
                    if (newNode == newNode.parent?.right) {
                        newNode = newNode.parent ?: throw NullNodeException()
                        leftRotate(newNode)
                    }
                    newNode.parent?.color = Color.BLACK
                    newNode.parent?.parent?.color = Color.RED
                    newNode.parent?.parent?.let { rightRotate(it) } ?: throw NullNodeException()
                }
            } else {                                            //если отец - правый сын дедушки
                val uncle = newNode.parent?.parent?.left
                if (uncle?.color == Color.RED) {
                    newNode.parent?.color = Color.BLACK
                    uncle.color = Color.BLACK
                    newNode.parent?.parent?.color = Color.RED
                    newNode = newNode.parent?.parent ?: throw NullNodeException()
                } else {                                        //если дядя черный или его нет
                    if (newNode == newNode.parent?.left) {
                        newNode = newNode.parent ?: throw NullNodeException()
                        rightRotate(newNode)
                    }
                    newNode.parent?.color = Color.BLACK
                    newNode.parent?.parent?.color = Color.RED
                    newNode.parent?.parent?.let { leftRotate(it) } ?: throw NullNodeException()
                }
            }
        }
        root?.color = Color.BLACK
    }

    private fun fixDelete(node: RBNode<K, V>?) {
        var x = node
        while (x != root && x?.color == Color.BLACK) {
            if (x == x.parent!!.left) {
                var w = x.parent!!.right ?: throw NullNodeException()
                if (w.color == Color.RED) {
                    w.color = Color.BLACK
                    x.parent!!.color = Color.RED
                    leftRotate(x.parent!!)
                    w = x.parent!!.right ?: throw NullNodeException()
                }
                if ((w.left?.color ?: Color.BLACK) == Color.BLACK && (w.right?.color ?: Color.BLACK) == Color.BLACK) {
                    w.color = Color.RED
                    x = x.parent!!
                } else {
                    if ((w.right?.color ?: Color.BLACK) == Color.BLACK) {
                        w.left?.color = Color.BLACK
                        w.color = Color.RED
                        rightRotate(w)
                        w = x.parent!!.right ?: throw NullNodeException()
                    }
                    w.color = x.parent!!.color
                    x.parent!!.color = Color.BLACK
                    w.right?.color = Color.BLACK
                    leftRotate(x.parent!!)
                    x = root ?: throw NullNodeException()
                }
            } else {
                var w = x.parent!!.left ?: throw NullNodeException()
                if (w.color == Color.RED) {
                    w.color = Color.BLACK
                    x.parent!!.color = Color.RED
                    rightRotate(x.parent!!)
                    w = x.parent!!.left ?: throw NullNodeException()
                }
                if ((w.right?.color ?: Color.BLACK) == Color.BLACK && (w.left?.color ?: Color.BLACK) == Color.BLACK) {
                    w.color = Color.RED
                    x = x.parent!!
                } else {
                    if ((w.left?.color ?: Color.BLACK) == Color.BLACK) {
                        w.right?.color = Color.BLACK
                        w.color = Color.RED
                        leftRotate(w)
                        w = x.parent!!.left ?: throw NullNodeException()
                    }
                    w.color = x.parent!!.color
                    x.parent!!.color = Color.BLACK
                    w.left?.color = Color.BLACK
                    rightRotate(x.parent!!)
                    x = root ?: throw NullNodeException()
                }
            }
        }
        x?.color = Color.BLACK
        root?.color = Color.BLACK
    }


    private fun leftRotate(node: RBNode<K, V>) {
        val rightChild = node.right ?: throw NullNodeException()
        node.right = rightChild.left
        if (rightChild.left != null) {
            rightChild.left!!.parent = node
        }
        rightChild.parent = node.parent
        if (node.parent == null) {
            root = rightChild
        } else if (node == node.parent!!.left) {
            node.parent!!.left = rightChild
        } else {
            node.parent!!.right = rightChild
        }
        rightChild.left = node
        node.parent = rightChild
    }

    private fun rightRotate(node: RBNode<K, V>) {
        val leftChild = node.left ?: throw NullNodeException()
        node.left = leftChild.right
        if (leftChild.right != null) {
            leftChild.right!!.parent = node
        }
        leftChild.parent = node.parent
        if (node.parent == null) {
            root = leftChild
        } else if (node == node.parent!!.right) {
            node.parent!!.right = leftChild
        } else {
            node.parent!!.left = leftChild
        }
        leftChild.right = node
        node.parent = leftChild
    }

    private fun transplant(u: RBNode<K, V>?, v: RBNode<K, V>?) {
        if (u?.parent == null) {
            root = v ?: throw NullNodeException()
        } else if (u == u.parent!!.left) {
            u.parent!!.left = v ?: throw NullNodeException()
        } else {
            u.parent!!.right = v ?: throw NullNodeException()
        }
        v.parent = u?.parent
    }
    fun contains(key: K): Boolean {
        var node = root
        while (node != null) {
            if (key == node.key) {
                return true
            }
            node = if (key < node.key) node.left else node.right
        }
        return false
    }
}