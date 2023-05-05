package trees

import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import exceptions.NullNodeException
import nodes.AVLNode
import kotlin.math.max

class AVLTree<K: Comparable<K>, V> : AbstractTree<K, V, AVLNode<K, V>>() {
    override fun add(key: K, value: V?) {
        fun addRecursive(node: AVLNode<K, V>? , key: K, value: V?): AVLNode<K, V> {
            if (node == null) {
                return AVLNode(key, value)
            }

            val delta: Int = key.compareTo(node.key)

            if (delta < 0) {
                node.left = addRecursive(node.left , key , value)
            } else if(delta > 0) {
                node.right = addRecursive(node.right , key , value)
            } else {
                throw NodeAlreadyExistsException()
            }

            return rebalance(node) ?: throw NullNodeException()
        }

        root = addRecursive(root , key , value)
    }

    override fun remove(key: K) {
        fun removeRecursive(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
            if (node == null) {
                throw NodeNotFoundException()
            }

            if (key < node.key) {
                node.left = removeRecursive(node.left , key)
            } else if (key > node.key) {
                node.right = removeRecursive(node.right , key)
            } else {
                if (node.left == null) {
                    return node.right
                } else if (node.right == null) {
                    return node.left
                }

                val minNode = findMin(node.right) ?: throw NullNodeException()
                node.key = minNode.key
                node.value = minNode.value
                node.right = removeRecursive(node.right, minNode.key)

            }

            return rebalance(node) ?: throw NullNodeException()
        }

        root = removeRecursive(root, key)
    }

    override fun search(key: K): AVLNode<K, V> {
        fun searchRecursive(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
            if (node == null) {
                return null
            }

            return if (key < node.key) {
                searchRecursive(node.left, key)
            } else if (key > node.key) {
                searchRecursive(node.right, key)
            } else {
                node
            }
        }

        return searchRecursive(root, key) ?: throw NodeNotFoundException()
    }

    private fun findMin(node: AVLNode<K, V>?): AVLNode<K, V>? = if (node?.left != null) findMin(node.left) else node

    private fun rebalance(node: AVLNode<K, V>): AVLNode<K, V>? {
        node.height = 1 + max(getHeight(node.left) , getHeight(node.right))

        val balance: Int = getBalance(node)
        val leftBalance: Int = getBalance(node.left)
        val rightBalance: Int = getBalance(node.right)

        if (balance > 1) {
            return if (leftBalance >= 0) {
                rotateRight(node)
            } else{
                node.left = rotateLeft(node.left)
                rotateRight(node)
            }
        }
        if (balance < -1) {
            return if (rightBalance <= 0) {
                rotateLeft(node)
            } else {
                node.right = rotateRight(node.right)
                rotateLeft(node)
            }
        }


        return node
    }

    private fun getBalance(node: AVLNode<K, V>?): Int {
        if (node == null) {
            return 0
        }
        return getHeight(node.left) - getHeight(node.right)
    }

    private fun getHeight(node: AVLNode<K, V>?): Int {
        if (node == null) {
            return 0
        }
        return node.height
    }

    private fun rotateRight(node: AVLNode<K, V>?): AVLNode<K, V>? {
        val x = node?.left
        val b = x?.right

        x?.right = node
        node?.left = b

        node?.height = 1 + max(getHeight(node?.left) , getHeight(node?.right))
        x?.height = 1 + max(getHeight(x?.left) , getHeight(x?.right))

        return x
    }

    private fun rotateLeft(node: AVLNode<K, V>?): AVLNode<K, V>? {
        val y = node?.right
        val b = y?.left

        y?.left = node
        node?.right = b

        node?.height = 1 + max(getHeight(node?.left) , getHeight(node?.right))
        y?.height = 1 + max(getHeight(y?.left) , getHeight(y?.right))

        return y
    }
}