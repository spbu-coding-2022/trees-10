package guiControl.painters

import nodes.Color
import nodes.RBNode
import trees.RBTree
class RBTPainter(
    tree: RBTree<Int, Int>,
    nodeMargin: Int = 30,
    nodeSize: Int = 20,
    width: Int
) : AbstractPainter<RBNode<Int, Int>, RBTree<Int, Int>>(tree, nodeMargin, nodeSize, width) {
    override fun getNodeColor(node: RBNode<Int, Int>): java.awt.Color {
        return if (node.color == Color.RED){
            java.awt.Color.RED
        } else if (node.color == Color.BLACK){
            java.awt.Color.BLACK
        } else java.awt.Color.YELLOW
    }
}