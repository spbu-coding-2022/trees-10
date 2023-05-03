package guiClasses.components.nodePanels

import guiControl.painters.AVLPainter
import nodes.AVLNode
import trees.AVLTree

class AVLPanel(private val tree: AVLTree<Int, Int>) :
    AbstractNodePanel<AVLNode<Int, Int>, AVLTree<Int, Int>, AVLPainter>(tree) {
    override fun painterInit() = AVLPainter(tree, Constants.nodeMargin, Constants.nodeSize, width)
}