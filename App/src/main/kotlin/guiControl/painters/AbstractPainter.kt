package guiControl.painters

import exceptions.NullNodeException
import guiControl.LineView
import guiControl.NodeView
import nodes.AbstractNode
import trees.AbstractTree
import java.awt.Point

abstract class AbstractPainter<NodeType : AbstractNode<Int, Int, NodeType>, TreeType : AbstractTree<Int, Int, NodeType>>(
    tree: TreeType,
    private val nodeMargin: Int,
    private val nodeSize: Int,
    width: Int
) {
    val nodes: MutableList<NodeView> = mutableListOf()
    val lines: MutableList<LineView> = mutableListOf()

    init {
        if (tree.root == null) {
            // Если дерево ещё не заполнено, то заполним его "образцом"
            tree.add(100)
            tree.add(-10)
            tree.add(120)
        }
        getViewNodes(tree.root ?: throw NullNodeException(), width / 2, nodeMargin)

    }

    /**
     * Позволяет определить каким цветом рисовать ноду
     */
    protected abstract fun getNodeColor(node: NodeType): java.awt.Color

    /**
     * Рекурсивно пробегает дерево и заполняет nodes и lines эл-ами для отрисовки
     */
    private fun getViewNodes(node: NodeType, x: Int, y: Int, n: Int = 1) {
        nodes.add(NodeView(Point(x, y), getNodeColor(node), node.key.toString()))

        var nextX: Int
        var nextY: Int

        if (node.left != null) {
            nextX = x - (x / (2 * n))
            nextY = y + nodeMargin + nodeSize

            lines.add(LineView(Point(x, y), Point(nextX, nextY)))

            getViewNodes(
                node.left ?: throw NullNodeException(),
                nextX,
                nextY,
                n + 1
            )
        }
        if (node.right != null) {

            nextX = x + (x / (2 * n))
            nextY = y + nodeMargin + nodeSize

            getViewNodes(
                node.right ?: throw NullNodeException(),
                nextX,
                nextY,
                (2 * n) + 1
            )

            lines.add(LineView(Point(x, y), Point(nextX, nextY)))
        }
    }

}