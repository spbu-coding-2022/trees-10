package trees

import nodes.Color
import nodes.RBNode
import exceptions.*


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
        val replacementNode = when {                //объявляем ноду, которая заменит удаяемую
            node.left == null -> node.right         //если один ребенок
            node.right == null -> node.left
            else -> {                               //если два ребенка
                var successor = node.right
                while (successor?.left != null) {   //ищем самого левого потомка правого ребенка удаляемой ноды
                    successor = successor.left
                }
                if (successor?.parent != node) {    //если у правого ребенка удаляемой ноды есть дети
                    transplant(successor ?: throw IllegalArgumentException("Node cannot be null"), successor.right)
                    successor.right = node.right
                    successor.right?.parent = successor
                }
                transplant(node, successor)
                successor.left = node.left
                successor.left?.parent = successor
                successor.color = node.color
                successor
            }
        }

        if (node.color == Color.BLACK) {
            fixDelete(replacementNode)
        }

        if (node.parent == null) {
            root = replacementNode
        } else if (node == (node.parent as RBNode<K, V>).left) {
            (node.parent as RBNode<K, V>).left = replacementNode
        } else {
            (node.parent as RBNode<K, V>).right = replacementNode
        }

        replacementNode?.parent = node.parent
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
        var currentNode = node
        while (currentNode != root && currentNode?.color == Color.BLACK) {  //пока нода не корень и она черная
            if (currentNode == currentNode.parent?.left) {                  //если нода - левый сын
                var sibling = currentNode.parent?.right
                if (sibling?.color == Color.RED) {                          //если брат - красный
                    sibling.color = Color.BLACK
                    currentNode.parent?.color = Color.RED
                    currentNode.parent?.let { leftRotate(it) }
                    sibling = currentNode.parent?.right
                }
                if (sibling?.left?.color == Color.BLACK && sibling.right?.color == Color.BLACK) {   //если дети брата черные
                    sibling.color = Color.RED
                    currentNode = currentNode.parent
                } else {
                    if (sibling?.right?.color == Color.BLACK) {     //левый сын брата красный, правый - черный
                        sibling.left?.color = Color.BLACK
                        sibling.color = Color.RED
                        sibling.let { rightRotate(it) }
                        sibling = currentNode.parent?.right
                    }
                    sibling?.color = currentNode.parent?.color ?: throw TreeException("Parent color is null")
                    currentNode.parent?.color = Color.BLACK
                    sibling?.right?.color = Color.BLACK
                    currentNode.parent?.let { leftRotate(it) }
                    currentNode = root
                }
            } else {                                            //нода - правый сын
                var sibling = currentNode.parent?.left
                if (sibling?.color == Color.RED) {              //если брат - красный
                    sibling.color = Color.BLACK
                    currentNode.parent?.color = Color.RED
                    currentNode.parent?.let { rightRotate(it) }
                    sibling = currentNode.parent?.left
                }
                if (sibling?.right?.color == Color.BLACK && sibling.left?.color == Color.BLACK) { //дети брата черные
                    sibling.color = Color.RED
                    currentNode = currentNode.parent
                } else {
                    if (sibling?.left?.color == Color.BLACK) {  //левый сын брата черный, правый - красный
                        sibling.right?.color = Color.BLACK
                        sibling.color = Color.RED
                        sibling.let { leftRotate(it) }
                        sibling = currentNode.parent?.left
                    }
                    sibling?.color = currentNode.parent?.color ?: throw TreeException("Parent color is null")
                    currentNode.parent?.color = Color.BLACK
                    sibling?.left?.color = Color.BLACK
                    currentNode.parent?.let { rightRotate(it) }
                    currentNode = root
                }
            }
        }
        currentNode?.color = Color.BLACK
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