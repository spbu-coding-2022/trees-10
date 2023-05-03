package guiClasses.components.nodePanels

import guiControl.painters.RBTPainter
import nodes.RBNode
import trees.RBTree

class RBTPanel(private val tree: RBTree<Int, Int>) :
    AbstractNodePanel<RBNode<Int, Int>, RBTree<Int, Int>, RBTPainter>(tree) {
    override fun painterInit() = RBTPainter(tree, Constants.nodeMargin, Constants.nodeSize, width)
}