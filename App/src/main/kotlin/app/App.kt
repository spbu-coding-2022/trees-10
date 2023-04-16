package app

import trees.BinaryTree

fun main() {
    val tree = BinaryTree<Int, String>()
    tree.add(100, "Hello World!")
    println(tree.search(100))
}