package guiControl.painters

import nodes.AVLNode
import trees.AVLTree
import java.awt.Color

class AVLPainter(
    tree: AVLTree<Int, Int>,
    nodeMargin: Int = 30,
    nodeSize: Int = 20,
    width: Int
) : AbstractPainter<AVLNode<Int, Int>, AVLTree<Int, Int>>(tree, nodeMargin, nodeSize, width) {
    override fun getNodeColor(node: AVLNode<Int, Int>): Color = Color.gray
}