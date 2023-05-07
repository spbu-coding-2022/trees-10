package app

import databases.json.RBTBase
import databases.sqlite.BTBase
import exceptions.NodeAlreadyExistsException
import exceptions.NodeNotFoundException
import guiClasses.components.Frame
import guiClasses.components.KeyTextField
import guiClasses.components.MenuClass
import guiClasses.components.TreePanel
import guiControl.painters.AVLPainter
import guiControl.painters.BTPainter
import guiControl.painters.RBTPainter
import trees.AVLTree
import trees.BinaryTree
import trees.RBTree
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
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

/**
 * Константы с сообщениями ошибок, именами файлов бд
 */
object Constants {
    const val BinaryBaseName = "Binary Tree Data.db"
    const val RBTBaseName = "Red-Black Tree Data.yml"

    const val NotFoundErrorMessage = "Tree node with such key not found"
    const val NotChosenErrorMessage = "You must select a tree to perform this action"
    const val AlreadyExistsErrorMessage = "Tree node with the same key already exists"
    const val InputErrorMessage = "Entered value is not a number or is too large"
    const val DataReadError = "Unable to read data from file"
    const val TreeAlreadyClearErrorMessage = "There are no more nodes in tree"

}

/**
 * Дерево, выбранное пользователем на данный момент
 */
private var currentTree: TreeTypes = TreeTypes.None

private lateinit var treeFrame: JFrame
private lateinit var treePanel: TreePanel

private lateinit var menuFrame: JFrame
fun main() {
    menuFrameInit()
    treeFrameInit()
    loadDatabase()
}

/**
 * Вытаскивает деревья из баз данных
 */
private fun loadDatabase() {
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
            showMessage(Constants.DataReadError)
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
            showMessage(Constants.DataReadError)
        }
    }
}

private fun treeFrameInit() {
    treeFrame = Frame("Treeple", 1000, 700, 360, 50)
    treePanel = TreePanel()
    treeFrame.add(treePanel)

    treeFrame.addComponentListener(object : ComponentAdapter() {
        override fun componentResized(componentEvent: ComponentEvent) {
            treeRepaint()
        }
    })

    Trees.binTree.run {
        add(100)
        add(120)
        add(-10)
    }

    Trees.RBTree.run {
        add(100)
        add(120)
        add(-10)
    }

    Trees.AVLTree.run {
        add(100)
        add(120)
        add(-10)
    }

}

/**
 * Выводит сообщение об ошибке на экран
 */
private fun showMessage(text: String, frame: JFrame = menuFrame, messageType: Int = JOptionPane.ERROR_MESSAGE) {
    JOptionPane.showMessageDialog(frame, text, "An error has occurred", messageType)
}

private fun treeRepaint() {
    when (currentTree) {
        TreeTypes.BINARY -> {
            if (Trees.binTree.root == null) {
                treePanel.clearTree()
                return
            }
            val painter = BTPainter(Trees.binTree, width = treeFrame.width)
            treePanel.changeTree(painter.lines, painter.nodes)
        }

        TreeTypes.AVL -> {
            if (Trees.AVLTree.root == null) {
                treePanel.clearTree()
                return
            }
            val painter = AVLPainter(Trees.AVLTree, width = treeFrame.width)
            treePanel.changeTree(painter.lines, painter.nodes)
        }

        TreeTypes.RB -> {
            if (Trees.RBTree.root == null) {
                treePanel.clearTree()
                return
            }
            val painter = RBTPainter(Trees.RBTree, width = treeFrame.width)
            treePanel.changeTree(painter.lines, painter.nodes)
        }

        else -> {}
    }
}

/**
 * Заполняет menuFrame компонентами
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

                    else -> showMessage(Constants.NotChosenErrorMessage)
                }

            } catch (ex: NodeAlreadyExistsException) {
                showMessage(Constants.AlreadyExistsErrorMessage)
            }
        } else
            showMessage(Constants.InputErrorMessage)

        addTextField.text = ""
        treeRepaint()
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

                    else -> showMessage(Constants.NotChosenErrorMessage)
                }

            } catch (ex: NodeNotFoundException) {
                showMessage(Constants.NotFoundErrorMessage)
            }
        } else
            showMessage(Constants.InputErrorMessage)

        removeTextField.text = ""
        treeRepaint()
    }

    val saveButton = JButton("Save")
    val clearButton = JButton("Clear")

    clearButton.addActionListener {
        when (currentTree) {
            TreeTypes.RB -> {
                if (Trees.RBTree.root == null)
                    showMessage(Constants.TreeAlreadyClearErrorMessage)
                else
                    Trees.RBTree = RBTree()
            }

            TreeTypes.BINARY -> {
                if (Trees.binTree.root == null)
                    showMessage(Constants.TreeAlreadyClearErrorMessage)
                else
                    Trees.binTree = BinaryTree()
            }

            TreeTypes.AVL -> {
                if (Trees.AVLTree.root == null)
                    showMessage(Constants.TreeAlreadyClearErrorMessage)
                else
                    Trees.AVLTree = AVLTree()
            }

            else -> showMessage(Constants.NotChosenErrorMessage)
        }
        treeRepaint()
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
                showMessage("AVL tree saving is not implemented ;(")
            }

            else -> showMessage(Constants.NotChosenErrorMessage)
        }
    }

    menuFrame.jMenuBar = MenuClass(
        onBinSelected = {
            currentTree = TreeTypes.BINARY
            treeRepaint()
        },
        onAVLSelected = {
            currentTree = TreeTypes.AVL
            treeRepaint()
        },
        onRBTSelected = {
            currentTree = TreeTypes.RB
            treeRepaint()
        }
    )

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