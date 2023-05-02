package guiClasses.components.nodePanels

import exceptions.NullNodeException
import guiControl.LineView
import guiControl.NodeView
import guiControl.painters.AbstractPainter
import nodes.AbstractNode
import nodes.RBNode
import trees.AbstractTree
import java.awt.*
import javax.swing.JPanel

abstract class AbstractNodePanel<
        NodeType : AbstractNode<Int, Int, NodeType>,
        TreeType : AbstractTree<Int, Int, NodeType>,
        Painter : AbstractPainter<NodeType, TreeType>>
    (private val tree: TreeType) : JPanel() {

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

        val painter: Painter = painterInit()

        for (line in painter.lines)
            drawLine(line)

        for (node in painter.nodes)
            drawNode(node)
    }

    protected abstract fun painterInit() : Painter
    private fun drawNode(node: NodeView) {
        graphics.color = node.color

        // Рисуем овал (ноду)
        graphics.fillOval(
            node.point.x - Constants.nodeSize / 2,
            node.point.y - Constants.nodeSize / 2,
            Constants.nodeSize,
            Constants.nodeSize
        )
        graphics.color = Color.BLACK
        graphics.drawString(node.value, node.point.x - node.value.length * 4, node.point.y + Constants.nodeSize)
    }

    private fun drawLine(line: LineView) {
        graphics.color = Constants.lineColor
        graphics.drawLine(line.from.x, line.from.y, line.to.x, line.to.y)
    }

    private fun getHeight(node: RBNode<Int, Int>): Int =
        1 + maxOf(getHeight(node.left ?: throw NullNodeException()), getHeight(node.right ?: throw NullNodeException()))
}
