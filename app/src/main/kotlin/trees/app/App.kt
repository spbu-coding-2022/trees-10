import BinaryTree.BinaryTree

fun main() {
    val tree = BinaryTree<Int, String>()
    tree.add(100, "root")
    tree.add(200, "root -> right")
    tree.add(50, "root -> left")

    println(tree.search(200))
}
