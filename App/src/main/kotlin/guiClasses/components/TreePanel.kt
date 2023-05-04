package guiClasses.components

import guiControl.LineView
import guiControl.NodeView
import java.awt.*
import javax.swing.JPanel

/**
 * Фрейм на котором отрисовываются все деревья
 */
class TreePanel(
    private val nodeSize: Int = 30,
    private val lineColor: Color = Color.BLACK,
    size: Dimension = Dimension(100, 700)
) : JPanel() {

    private var linesToDraw: List<LineView> = listOf()
    private var nodesToDraw: List<NodeView> = listOf()

    init {
        preferredSize = size
    }

    fun changeTree(newLines: List<LineView>, newNodes: List<NodeView>) {
        this.linesToDraw = newLines
        this.nodesToDraw = newNodes

        repaint()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        // Сглаживание
        val g2d = (g as Graphics2D).also {
            val rh = RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            )
            it.setRenderingHints(rh)
        }

        if (nodesToDraw.isEmpty()) {
            val x = width / 2 - 100
            val y = height / 2

            g2d.color = Color.gray
            g2d.font = Font("Tahoma", Font.TRUETYPE_FONT, 24)
            g2d.drawString("Tree is empty ;(", x, y)

            return
        }

        for (line in linesToDraw)
            drawLine(line, g2d)

        for (node in nodesToDraw)
            drawNode(node, g2d)
    }

    private fun drawNode(node: NodeView, graphics: Graphics2D) {
        graphics.color = node.color

        // Рисуем овал (ноду)
        graphics.fillOval(
            node.point.x - nodeSize / 2,
            node.point.y - nodeSize / 2,
            nodeSize,
            nodeSize
        )

        graphics.color = Color.BLACK
        graphics.drawString(
            node.value,
            node.point.x - node.value.length * 4,
            node.point.y + nodeSize
        )
    }

    private fun drawLine(line: LineView, graphics: Graphics2D) {
        graphics.color = lineColor
        graphics.drawLine(line.from.x, line.from.y, line.to.x, line.to.y)
    }
}