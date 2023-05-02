package guiClasses.components.nodePanels

import guiControl.painters.BTPainter
import nodes.BinaryNode
import trees.BinaryTree

class BTPanel(private val tree: BinaryTree<Int, Int>) :
    AbstractNodePanel<BinaryNode<Int, Int>, BinaryTree<Int, Int>, BTPainter>(tree) {
    override fun painterInit() = BTPainter(tree, Constants.nodeMargin, Constants.nodeSize, width)
}