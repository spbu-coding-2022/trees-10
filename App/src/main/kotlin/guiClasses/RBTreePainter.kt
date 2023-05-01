package guiClasses

import javax.swing.*
import java.awt.*
import trees.RBTree
import nodes.RBNode
import nodes.Color
import trees.AbstractTree

class RBTNodePanel(private val tree: RBTree<Int, String>): JPanel(){
    val nodeSize = 30
    val nodeMargin = 10
    val lineColor = java.awt.Color.BLACK

    init {
        preferredSize = Dimension(400, 400)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (tree.root != null){
            drawNode(g, tree.root!!, width / 2, nodeMargin, width / 2)
        }
    }

    fun drawNode(g: Graphics, node: RBNode<Int, String>, x: Int, y: Int, parentX: Int){
        g.color = if (node.color == Color.BLACK) {        //у ноды цвет задан классом Color из RBNode,
            java.awt.Color.BLACK                          //у g - из библиотеки, поэтому вводим проверку
        } else java.awt.Color.RED
        g.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize)      // рисуем ноду
        g.color = lineColor
        if (node.parent != null){
            g.drawLine(x, y, parentX, y - nodeMargin)       //рисуем линию, если есть родитель у ноды
        }
        if (node.left != null) {
            drawNode(g, node.left!!, x - width / (1 shl (getHeight(node.left!!) + 2)), y + nodeMargin + nodeSize, x)
        }
        if (node.right != null) {
            drawNode(g, node.right!!, x + width / (1 shl (getHeight(node.right!!) + 2)), y + nodeMargin + nodeSize, x)
        }
    }
    private fun getHeight(node: RBNode<Int, String>): Int {
        if (node == null) {
            return -1
        }
        return 1 + maxOf(getHeight(node.left!!), getHeight(node.right!!))
    }
}
