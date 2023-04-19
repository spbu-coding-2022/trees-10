package trees

import nodes.AVLNode
import kotlin.math.max

class AVLTree<K: Comparable<K>, V> : AbstractTree<K, V, AVLNode<K, V>>() {
    override var root: AVLNode<K, V>? = null


    override fun add(key: K, value: V?) {
        root = insert(root , key , value)
    }

    override fun remove(key: K) {
        root = delete(root, key)
    }

    override fun search(key: K): AVLNode<K, V>? {
        return searchNode(root, key)
    }

    private fun searchNode(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
        if (node == null) {
            return null
        }

        if (key < node.key) {
            return searchNode(node.left, key)
        } else if (key > node.key) {
            return searchNode(node.right, key)
        } else {
            return node
        }
    }

    private fun delete(node: AVLNode<K, V>?, key: K): AVLNode<K, V>? {
        if (node == null) {
            return null
        }

        if (key < node.key) {
            node.left = delete(node.left , key)
        } else if (key > node.key) {
            node.right = delete(node.right , key)
        } else {
            if (node.left == null) {
                return node.right
            } else if (node.right == null) {
                return node.left
            }

            val temp = findMin(node)
            node.value = temp.value
            node.right = delete(node.right, temp.key)

        }

        return rebalance(node) ?: throw Exception("Rebalance returned a null value")
    }

    private fun insert(node: AVLNode<K, V>? , key: K, value: V?): AVLNode<K, V> {
        if (node == null) {
            return AVLNode(key, value)
        }

        val delta: Int = key.compareTo(node.key)

        if (delta < 0) {
            node.left = insert(node.left , key , value)
        } else if(delta > 0) {
            node.right = insert(node.right , key , value)
        } else {
            //Duplicate key
        }

        return rebalance(node) ?: throw Exception("Rebalance returned a null value")
    }

    private fun findMin(node: AVLNode<K, V>): AVLNode<K, V> = if (node.left != null) findMin(node.left!!) else node

    private fun rebalance(node: AVLNode<K, V>): AVLNode<K, V>? {
        node.height = 1 + max(getHeight(node.left) , getHeight(node.right))

        val balance: Int = getBalance(node)
        val leftBalance: Int = getBalance(node.left)
        val rightBalance: Int = getBalance(node.right)

        if (balance > 1 && leftBalance >= 0) {
            return rotateRight(node)
        }
        if (balance > 1 && leftBalance < 0) {
            node.left = rotateLeft(node.left)
            return rotateRight(node)
        }
        if (balance < -1 && rightBalance <= 0) {
            return rotateLeft(node)
        }
        if (balance < -1 && rightBalance > 0) {
            node.right = rotateRight(node.right)
            return rotateLeft(node)
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