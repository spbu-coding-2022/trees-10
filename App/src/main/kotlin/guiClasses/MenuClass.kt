package Menu

import java.awt.Color
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class MenuClass() : JMenuBar() {

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
    }

    private val menu = JMenu("Выбор Дерева")
    private val menuItems = arrayOf(JMenuItem("Binary Tree"), JMenuItem("AVL-Tree"), JMenuItem("Red-black Tree"))

    init {
        menuItems.forEach { menu.add(it) }
        resetMenuItemsChoosing()

        // Слушатель событий для элемента меню "Binary Tree"
        menuItems[0].addActionListener {
            updateMenuItemsChoosing(menuItems[0])
        }

        // Слушатель событий для элемента меню "AVL-Tree"
        menuItems[1].addActionListener {
            updateMenuItemsChoosing(menuItems[1])
        }

        // Слушатель событий для элемента меню "Red-black Tree"
        menuItems[2].addActionListener {
            updateMenuItemsChoosing(menuItems[2])
        }

        add(menu)
    }

}