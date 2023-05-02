package guiClasses

import exceptions.NullNodeException
import nodes.Color
import nodes.RBNode
import trees.RBTree
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel

class RBTNodePanel(private val tree: RBTree<Int, Int>) : JPanel() {

    /**
     * Объект со свойствами нод
     */
    object Constants {
        const val nodeSize = 30
        const val nodeMargin = 20
        val lineColor = java.awt.Color.BLACK
            ?: throw NullPointerException()
    }

    init {
        preferredSize = Dimension(1000, 700)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        // Сглаживание
        (g as Graphics2D).also {
            val rh = RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            )
            it.setRenderingHints(rh)
        }

        if (tree.treeRoot != null) {
            val rootX = width / 2 // нода рисуется посередине
            drawNode(g, tree.treeRoot ?: throw NullNodeException(), rootX, Constants.nodeMargin)
        }
    }


    /**
     * Рекурсивная отрисовка каждой ноды дерева
     */
    private fun drawNode(g: Graphics, node: RBNode<Int, Int>, x: Int, y: Int) {
        // Устанавливаем цвет рисуемой ноды как у переданной
        g.color = if (node.color == Color.BLACK)
            java.awt.Color.BLACK
        else
            java.awt.Color.RED

        // Рисуем овал (ноду)
        g.fillOval(x - Constants.nodeSize / 2, y - Constants.nodeSize / 2, Constants.nodeSize, Constants.nodeSize)
        g.color = Constants.lineColor //Обводка овала

        if (node.left != null) {
            val nextX = x - Constants.nodeMargin - Constants.nodeSize
            val nextY = y + Constants.nodeMargin + Constants.nodeSize

            g.drawLine(x, y, nextX, nextY)

            drawNode(
                g,
                node.left ?: throw NullNodeException(),
                nextX,
                nextY
            )
        }                   //рекурсивно рисуем левое поддерево, shl - побитовый сдвиг влево
        if (node.right != null) {
            val nextX = x + Constants.nodeMargin + Constants.nodeSize
            val nextY = y + Constants.nodeMargin + Constants.nodeSize

            g.drawLine(x, y, nextX, nextY)

            drawNode(
                g,
                node.right ?: throw NullNodeException(),
                nextX,
                nextY
            )
        }                   //рекурсивно рисуем правое поддерево
    }

    private fun getHeight(node: RBNode<Int, Int>): Int =
        1 + maxOf(getHeight(node.left ?: throw NullNodeException()), getHeight(node.right ?: throw NullNodeException()))
}
