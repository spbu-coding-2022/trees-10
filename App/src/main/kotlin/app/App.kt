package app

import guiClasses.Frame
import Menu.*
import guiClasses.KeyTextField
import javax.swing.*

private fun treeFrameInit() {
    val treeFrame = Frame("Treeple", 1000, 700, 360, 50)

    // Создаем панель с компонентами
    val treePanel = JPanel()

    val scrollPane = JScrollPane()
    // Добавляем панель на панель с прокруткой
    scrollPane.setViewportView(treePanel)

    // Добавляем панель с прокруткой на окно
    treeFrame.contentPane.add(scrollPane)

    // Устанавливаем режим прокрутки по вертикали и горизонтали
    scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

    treeFrame.add(treePanel)
}

private fun menuFrameInit() {
    val menuFrame = Frame("Treeple Menu", 300, 400, 50, 50)

    val addButton = JButton("Add")
    val addTextField = KeyTextField()

    val removeButton = JButton("remove")
    val removeTextField = KeyTextField()

    val searchButton = JButton("search")
    val searchTextField = KeyTextField()

    val treeMenu = MenuClass()

    // contentPane - контейнер для компонентов
    val layout = GroupLayout(menuFrame.contentPane)
    menuFrame.contentPane.layout = layout

    layout.autoCreateContainerGaps = true
    layout.autoCreateGaps = true

    layout.setHorizontalGroup(
        layout.createSequentialGroup() // Последовательные группы
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

    )
}

fun main() {
    treeFrameInit()
    menuFrameInit()
}