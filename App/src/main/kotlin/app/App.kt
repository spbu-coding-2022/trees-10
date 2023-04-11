package app

import BinaryTree.BinaryTree

fun main() {
    val tree = BinaryTree<Int, String>()
    tree.add(100, "Hello World!")
    println(tree.search(100))
}