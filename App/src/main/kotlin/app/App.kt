package app

import databases.json.RBTBase
import databases.sqlite.BTBase
import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import guiClasses.components.Frame
import guiClasses.components.KeyTextField
import guiClasses.components.MenuClass
import guiClasses.components.nodePanels.AVLPanel
import guiClasses.components.nodePanels.BTPanel
import guiClasses.components.nodePanels.RBTPanel
import trees.AVLTree
import trees.BinaryTree
import trees.RBTree
import java.io.File
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane

/**
 * Объект, хранящий отдельно каждое из деревьев
 * (позволяет параллельно работать сразу со всеми)
 */
private object Trees {
    var binTree: BinaryTree<Int, Int> = BinaryTree()
    var AVLTree: AVLTree<Int, Int> = AVLTree()
    var RBTree: RBTree<Int, Int> = RBTree()
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

object Constants {
    const val BinaryBaseName = "Binary Tree Data.db"
    const val RBTBaseName = "Red-Black Tree Data.yml"

    const val NotFoundErrorMessage = "Tree node with such key not found"
    const val NotChosenErrorMessage = "You must select a tree to perform this action"
    const val AlreadyExistsErrorMessage = "Tree node with the same key already exists"
    const val InputErrorMessage = "Entered value is not a number or is too large"
    const val DataReadError = "Unable to read data from file"
}

/**
 * Дерево, выбранное пользователем на данный момент
 */
private var currentTree: TreeTypes = TreeTypes.None

private lateinit var treeFrame: JFrame
private lateinit var menuFrame: JFrame
fun main() {
    menuFrameInit()

    if (File(Constants.RBTBaseName).exists()) {
        try {
            val base = RBTBase(Constants.RBTBaseName,
                serializeValue = { value -> value?.toString() ?: "null" },
                deserializeValue = { value ->
                    if (value == "null")
                        0
                    else
                        value.toInt()
                },
                deserializeKey = { value -> value.toInt() })

            Trees.RBTree = base.loadTree()
        } catch (ex: Exception) {
            showError(Constants.DataReadError)
        }
    }
    if (File(Constants.BinaryBaseName).exists()) {
        try {
            val base = BTBase(Constants.BinaryBaseName,
                serializeValue = { value -> value?.toString() ?: "null" },
                deserializeValue = { value ->
                    if (value == "null")
                        0
                    else
                        value.toInt()
                },
                deserializeKey = { value -> value.toInt() })

            Trees.binTree = base.loadTree()
        } catch (ex: Exception) {
            showError(Constants.DataReadError)
        }
    }
}

/**
 * Выполняет отрисовку переданного дерева на treeFrame
 */
private fun treeInit(newTree: TreeTypes) {
    if (::treeFrame.isInitialized)
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

/**
 * Выводит сообщение об ошибке на экран
 */
private fun showError(text: String, frame: JFrame = menuFrame) {
        JOptionPane.showMessageDialog(frame, text, "An error has occurred", JOptionPane.ERROR_MESSAGE)
}

/**
 * Заполнение Menu Frame компонентами
 */
private fun menuFrameInit() {
    menuFrame = Frame("Treeple Menu", 300, 400, 50, 50)

    val addButton = JButton("Add")
    val addTextField = KeyTextField(addButton)

    addButton.addActionListener {
        if (addTextField.text.toIntOrNull() != null) {
            val key = addTextField.text.toInt()
            try {
                when (currentTree) {
                    TreeTypes.RB -> Trees.RBTree.add(key)
                    TreeTypes.BINARY -> Trees.binTree.add(key)
                    TreeTypes.AVL -> Trees.AVLTree.add(key)

                    else -> showError(Constants.NotChosenErrorMessage)
                }

            } catch (ex: NodeAlreadyExistsException) {
                showError(Constants.AlreadyExistsErrorMessage)
            }
        } else
            showError(Constants.InputErrorMessage)
    }

    val removeButton = JButton("Remove")
    val removeTextField = KeyTextField(removeButton)

    removeButton.addActionListener {
        if (removeTextField.text.toIntOrNull() != null) {
            val key = removeTextField.text.toInt()
            try {
                when (currentTree) {
                    TreeTypes.RB -> Trees.RBTree.remove(key)
                    TreeTypes.BINARY -> Trees.binTree.remove(key)
                    TreeTypes.AVL -> Trees.AVLTree.remove(key)

                    else -> showError(Constants.NotChosenErrorMessage)
                }

            } catch (ex: NodeNotFoundException) {
                showError(Constants.NotFoundErrorMessage)
            }
        } else
            showError(Constants.InputErrorMessage)
    }

    val saveButton = JButton("Save")
    val clearButton = JButton("Clear")

    clearButton.addActionListener {
        treeInit(currentTree)
    }

    saveButton.addActionListener {
        when (currentTree) {
            TreeTypes.BINARY -> {
                val base = BTBase(Constants.BinaryBaseName,
                    serializeValue = { value -> value?.toString() ?: "null" },
                    deserializeValue = { value ->
                        if (value == "null")
                            0
                        else
                            value.toInt()
                    },
                    deserializeKey = { value -> value.toInt() })
                base.saveTree(Trees.binTree)
            }

            TreeTypes.RB -> {
                val base = RBTBase(
                    Constants.RBTBaseName,
                    serializeValue = { value -> value?.toString() ?: "null" },
                    deserializeValue = { value ->
                        if (value == "null")
                            0
                        else
                            value.toInt()
                    },
                    deserializeKey = { value -> value.toInt() })
                base.saveTree(Trees.RBTree)
            }

            TreeTypes.AVL -> {
                showError("AVL tree saving is not implemented ;(")
            }

            else -> showError(Constants.NotChosenErrorMessage)
        }
    }

    menuFrame.jMenuBar = MenuClass(::treeInit)

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
                    )
                    .addGroup( // Группа с TextFields
                        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(addTextField)
                            .addComponent(removeTextField)
                    )
            )
            .addComponent(saveButton)
            .addComponent(clearButton)
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
                layout.createSequentialGroup()
                    .addComponent(saveButton)
                    .addComponent(clearButton)
            )

    )

}
