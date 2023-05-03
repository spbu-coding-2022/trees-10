package guiControl.painters

import nodes.Color
import nodes.RBNode
import trees.RBTree
class RBTPainter(
    tree: RBTree<Int, Int>,
    nodeMargin: Int,
    nodeSize: Int,
    width: Int
) : AbstractPainter<RBNode<Int, Int>, RBTree<Int, Int>>(tree, nodeMargin, nodeSize, width) {
    override fun getNodeColor(node: RBNode<Int, Int>): java.awt.Color {
        return if (node.color == Color.RED)
            java.awt.Color.RED
        else
            java.awt.Color.BLACK
    }
}