package app

import exceptions.NodeAlreadyExistsException
import guiClasses.components.Frame
import guiClasses.components.KeyTextField
import guiClasses.components.MenuClass
import guiClasses.components.nodePanels.AVLPanel
import guiClasses.components.nodePanels.BTPanel
import guiClasses.components.nodePanels.RBTPanel
import trees.AVLTree
import trees.BinaryTree
import trees.RBTree
import javax.swing.*

/**
 * Объект, хранящий отдельно каждое из деревьев
 * (позволяет параллельно работать сразу со всеми)
 */
private object Trees {
    val binTree: BinaryTree<Int, Int> = BinaryTree()
    val AVLTree: AVLTree<Int, Int> = AVLTree()
    val RBTree: RBTree<Int, Int> = RBTree()
}

/**
 * Возможные виды деревьев, которые доступны пользователю RBTree
 */
enum class TreeTypes {
    BINARY,
    AVL,
    RB,
    None
}

/**
 * Дерево, выбранное пользователем на данный момент
 */
private var currentTree: TreeTypes = TreeTypes.None

private lateinit var treeFrame: JFrame
private lateinit var menuFrame: JFrame
fun main() {
    menuFrame = Frame("Treeple Menu", 300, 400, 50, 50)
    menuFrameInit()
    treeFrame = Frame("Treeple", 1000, 700, 360, 50)
}

private fun showError(text: String, frame: JFrame) {
    JOptionPane.showMessageDialog(frame, text, "Произошла ошибка" , JOptionPane.ERROR_MESSAGE)
}
private fun menuFrameInit() {

    val addButton = JButton("Add")
    val addTextField = KeyTextField()

    addButton.addActionListener {
        if (addTextField.text.toIntOrNull() != null) {
            val key = addTextField.text.toInt()
            try {
                when (currentTree) {
                    TreeTypes.RB -> Trees.RBTree.add(key)
                    TreeTypes.BINARY -> Trees.binTree.add(key)
                    TreeTypes.AVL -> Trees.AVLTree.add(key)

                    else -> showError("Сначала выберите дерево", menuFrame)
                }

                treeInit(currentTree)

            } catch (ex: NodeAlreadyExistsException) {
                showError("Узел с таким значением уже существует", menuFrame)
            }
        } else
            showError("Добавлять можно только узлы с числовыми значениями", menuFrame)
    }

    val removeButton = JButton("Remove")
    val removeTextField = KeyTextField()

    val searchButton = JButton("Find")
    val searchTextField = JTextField("Test")

    val saveButton = JButton("Save")

    menuFrame.jMenuBar = MenuClass (::treeInit)

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
private fun treeInit(newTree: TreeTypes) {
    treeFrame.dispose()
    treeFrame = Frame("Treeple", 1000, 700, 360, 50)
    when (newTree) {
        TreeTypes.RB -> treeFrame.add(RBTPanel(Trees.RBTree))
        TreeTypes.AVL -> treeFrame.add(AVLPanel(Trees.AVLTree))
        TreeTypes.BINARY -> treeFrame.add(BTPanel(Trees.binTree))
        else -> return
    }
    println(newTree)
    currentTree = newTree

}
