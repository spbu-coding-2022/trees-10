package app

import guiClasses.Frame
import guiClasses.KeyTextField
import guiClasses.MenuClass
import guiClasses.RBTNodePanel
import trees.AVLTree
import trees.BinaryTree
import trees.RBTree
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JFrame

private lateinit var treeFrame: JFrame
private lateinit var menuFrame: JFrame

private lateinit var rbTree: RBTree<Int, Int>
private fun menuFrameInit() {
    menuFrame = Frame("Treeple Menu", 300, 400, 50, 50)

    val addButton = JButton("Add")
    val addTextField = KeyTextField()

    val removeButton = JButton("Remove")
    val removeTextField = KeyTextField()

    val searchButton = JButton("Find")
    val searchTextField = KeyTextField()

    val saveButton = JButton("Save")

    val treeMenu = MenuClass { tree ->
        when (tree) {
            is BinaryTree<*, *> -> {
                println("бинарное дерево")
            }

            is AVLTree<*, *> -> {
                println("AVL дерево")
            }

            is RBTree<*, *> -> {
                rbtDraw()
            }
        }
    }

    menuFrame.jMenuBar = treeMenu

    // contentPane - контейнер для компонентов
    val layout = GroupLayout(menuFrame.contentPane)
    menuFrame.contentPane.layout = layout

    layout.autoCreateContainerGaps = true
    layout.autoCreateGaps = true

    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup( // Группа с кнопками и TextFields
                layout.createSequentialGroup()
                    .addGroup( // Группа с кнопками
                        // GroupLayout.Alignment.LEADING - выравнивание по левому краю в горизонтальном измерении
                        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(addButton)
                            .addComponent(removeButton)
                            .addComponent(searchButton)
                    )
                    .addGroup( // Группа с TextFields
                        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(addTextField)
                            .addComponent(removeTextField)
                            .addComponent(searchTextField)
                    )
            )
            .addComponent(saveButton)
    )

    layout.setVerticalGroup(
        layout.createSequentialGroup()
            .addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(addTextField)
            )

            .addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(removeButton)
                    .addComponent(removeTextField)
            )

            .addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(searchButton)
                    .addComponent(searchTextField)
            )

            .addGroup(
                layout.createSequentialGroup()
                    .addComponent(saveButton)
            )

    )
}

private fun rbtDraw() {
    rbTree = RBTree()
    rbTree.add(100)
    rbTree.add(0)
    rbTree.add(120)
    rbTree.add(130)
    rbTree.add(140)
    rbTree.add(3)
    // Создаем панель с компонентами
    val treePanel = RBTNodePanel(rbTree)

//    val panel = JScrollPane()
//    // Добавляем панель на панель с прокруткой
//    panel.setViewportView(treePanel)
//
//    // Добавляем панель с прокруткой на окно
//    treeFrame.contentPane.add(panel)
//
//    // Устанавливаем режим прокрутки по вертикали и горизонтали
//    panel.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
//    panel.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
//
    treeFrame.add(treePanel)
}

fun main() {
    menuFrameInit()
    treeFrame = Frame("Treeple", 1000, 700, 360, 50)
    rbtDraw()
}