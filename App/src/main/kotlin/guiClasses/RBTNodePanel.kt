package guiClasses

import exceptions.NullNodeException
import nodes.Color
import nodes.RBNode
import trees.RBTree
import java.awt.*
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

    private lateinit var graphics: Graphics

    init {
        preferredSize = Dimension(1000, 700)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        // Сглаживание
        graphics = (g as Graphics2D).also {
            val rh = RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            )
            it.setRenderingHints(rh)
        }

        if (tree.treeRoot != null) {
            val rootX = width / 2 // нода рисуется посередине
            val rootY = Constants.nodeMargin

            drawNode(tree.treeRoot?: throw NullNodeException(), Point(rootX, rootY))

            recursiveDraw(tree.treeRoot ?: throw NullNodeException(), rootX, Constants.nodeMargin)
        }
    }


    /**
     * Рекурсивная отрисовка каждой ноды дерева
     */
    private fun recursiveDraw(node: RBNode<Int, Int>, x: Int, y: Int, n: Int = 1) {

        var nextX: Int
        var nextY: Int

        if (node.left != null) {
            nextX = x - (x / (2 * n))
            nextY = y + Constants.nodeMargin + Constants.nodeSize

            drawLine(Point(x, y), Point(nextX, nextY))

            recursiveDraw(
                node.left ?: throw NullNodeException(),
                nextX,
                nextY,
                n + 1
            )
        }                   //рекурсивно рисуем левое поддерево, shl - побитовый сдвиг влево
        if (node.right != null) {

            nextX = x + (x / (2 * n))
            nextY = y + Constants.nodeMargin + Constants.nodeSize

            drawLine(Point(x, y), Point(nextX, nextY))

            recursiveDraw(
                node.right ?: throw NullNodeException(),
                nextX,
                nextY,
                n+1
            )
        }                   //рекурсивно рисуем правое поддерево

        drawNode(node, Point(x, y))

    }

    private fun drawNode(node: RBNode<Int, Int>, p: Point) {
        graphics.color = if (node.color == Color.BLACK)
            java.awt.Color.BLACK
        else
            java.awt.Color.RED

        // Рисуем овал (ноду)
        graphics.fillOval(
            p.x - Constants.nodeSize / 2,
            p.y - Constants.nodeSize / 2,
            Constants.nodeSize,
            Constants.nodeSize
        )
    }

    private fun drawLine(start: Point, end: Point) {
        graphics.color = Constants.lineColor
        graphics.drawLine(start.x, start.y, end.x, end.y)
    }

    private fun getHeight(node: RBNode<Int, Int>): Int =
        1 + maxOf(getHeight(node.left ?: throw NullNodeException()), getHeight(node.right ?: throw NullNodeException()))
}
