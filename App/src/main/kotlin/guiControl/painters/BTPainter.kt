package guiControl.painters

import nodes.BinaryNode
import nodes.Color
import trees.BinaryTree
class BTPainter(
    tree: BinaryTree<Int, Int>,
    nodeMargin: Int = 30,
    nodeSize: Int = 20,
    width: Int
) : AbstractPainter<BinaryNode<Int, Int>, BinaryTree<Int, Int>>(tree, nodeMargin, nodeSize, width) {
    override fun getNodeColor(node: BinaryNode<Int, Int>): java.awt.Color{
        return if (node.color == Color.YELLOW){
            java.awt.Color.YELLOW
        } else java.awt.Color.DARK_GRAY
    }
}