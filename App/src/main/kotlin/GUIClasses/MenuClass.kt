package Menu

import java.awt.Color
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class MenuClass() : JMenuBar() {
    init {
        val menu = JMenu("Выбор Дерева")
        val BinTree = JMenuItem("Binary Tree")
        val AVLTree = JMenuItem("AVL-Tree")
        val RBTree = JMenuItem("Red-black Tree")

        menu.add(BinTree)
        menu.add(AVLTree)
        menu.add(RBTree)

        add(menu)

        // Флаги выбранных элементов меню
        var binTreeSelected = false
        var avlTreeSelected = false
        var rbTreeSelected = false

        // Обновляем цвета при выборе другого варианта
        fun resetMenuItemsChoosing() {
            BinTree.background = Color.WHITE
            AVLTree.background = Color.WHITE
            RBTree.background = Color.WHITE

        }

        // Функция для обновления видимости элементов меню
        fun updateMenuItemsChoosing() {
            if (binTreeSelected) {BinTree.background = Color.PINK}
            if (avlTreeSelected) {AVLTree.background = Color.PINK}
            if (rbTreeSelected) {RBTree.background = Color.PINK}

        }

        // Слушатель событий для элемента меню "Binary Tree"
        BinTree.addActionListener {
            resetMenuItemsChoosing()
            binTreeSelected = true
            avlTreeSelected = false
            rbTreeSelected = false
            updateMenuItemsChoosing()
        }

        // Слушатель событий для элемента меню "AVL-Tree"
        AVLTree.addActionListener {
            resetMenuItemsChoosing()
            binTreeSelected = false
            avlTreeSelected = true
            rbTreeSelected = false
            updateMenuItemsChoosing()
        }

        // Слушатель событий для элемента меню "Red-black Tree"
        RBTree.addActionListener {
            resetMenuItemsChoosing()
            binTreeSelected = false
            avlTreeSelected = false
            rbTreeSelected = true
            updateMenuItemsChoosing()
        }
    }
}