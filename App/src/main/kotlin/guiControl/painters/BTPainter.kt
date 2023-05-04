package guiControl.painters

import nodes.BinaryNode
import trees.BinaryTree
import java.awt.Color
class BTPainter(
    tree: BinaryTree<Int, Int>,
    nodeMargin: Int,
    nodeSize: Int,
    width: Int
) : AbstractPainter<BinaryNode<Int, Int>, BinaryTree<Int, Int>>(tree, nodeMargin, nodeSize, width) {
    override fun getNodeColor(node: BinaryNode<Int, Int>): Color = Color.DARK_GRAY
}