package app

import guiClasses.Frame
import Menu.*
import java.awt.EventQueue
import javax.swing.*

fun main() {
    EventQueue.invokeLater {
        val treeFrame = Frame("Treeple", 1000, 700, 360, 50)
        val menuFrame = Frame("Treeple Menu", 300, 400, 50, 50)

        // Создаем панель с компонентами
        val treePanel = JPanel()
        val menuPanel = JPanel()

        val scrollPane = JScrollPane()
        // Добавляем панель на панель с прокруткой
        scrollPane.setViewportView(treePanel)

        // Добавляем панель с прокруткой на окно
        menuFrame.contentPane.add(scrollPane)

        // Устанавливаем режим прокрутки по вертикали и горизонтали
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        menuPanel.add(MenuClass())

        menuFrame.add(menuPanel)
        treeFrame.add(treePanel)
    }
}