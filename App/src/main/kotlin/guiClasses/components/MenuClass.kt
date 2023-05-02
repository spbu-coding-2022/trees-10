package guiClasses.components

import trees.AVLTree
import trees.BinaryTree
import trees.RBTree
import java.awt.Color
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class MenuClass(private val onTreeSelected: (tree: Any) -> Unit) : JMenuBar() {

    /**
     * Визуально обозначает элементы как неактивные
     * */
    private fun resetMenuItemsChoosing() {
        menuItems.forEach { it.background = Color.WHITE }
    }

    /**
     * Визуально обозначает цветом переданный эл-т как выделенный
     * */
    private fun updateMenuItemsChoosing(item: JMenuItem) {
        item.background = Color.PINK
        menuItems.filter { it != item }.forEach { it.background = Color.WHITE }
    }

    private val menu = JMenu("Выбор Дерева")
    private val menuItems = arrayOf(
        JMenuItem("Binary Tree"),
        JMenuItem("AVL-Tree"),
        JMenuItem("Red-black Tree")
    )

    init {
        menuItems.forEach { menu.add(it) }
        resetMenuItemsChoosing()

        // Слушатель событий для элемента меню "Binary Tree"
        menuItems[0].addActionListener {
            updateMenuItemsChoosing(menuItems[0])
            onTreeSelected(BinaryTree<Int, String>())
        }

        // Слушатель событий для элемента меню "AVL-Tree"
        menuItems[1].addActionListener {
            updateMenuItemsChoosing(menuItems[1])
            onTreeSelected(AVLTree<Int, String>())
        }

        // Слушатель событий для элемента меню "Red-black Tree"
        menuItems[2].addActionListener {
            updateMenuItemsChoosing(menuItems[2])
            onTreeSelected(RBTree<Int, String>())
        }

        add(menu)
    }

}