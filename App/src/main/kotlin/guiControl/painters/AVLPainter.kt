package guiControl.painters

import nodes.AVLNode
import nodes.Color
import trees.AVLTree

class AVLPainter(
    tree: AVLTree<Int, Int>,
    nodeMargin: Int = 30,
    nodeSize: Int = 20,
    width: Int
) : AbstractPainter<AVLNode<Int, Int>, AVLTree<Int, Int>>(tree, nodeMargin, nodeSize, width) {
    override fun getNodeColor(node: AVLNode<Int, Int>): java.awt.Color{
        return if (node.color == Color.YELLOW){
            java.awt.Color.YELLOW
        } else java.awt.Color.GRAY
    }
}