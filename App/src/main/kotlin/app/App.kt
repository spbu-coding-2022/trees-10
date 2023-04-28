package app

import guiClasses.Frame
import Menu.*
import java.awt.Container
import java.awt.EventQueue
import java.awt.LayoutManager
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JPanel

fun main() {
    EventQueue.invokeLater {
//        val treeFrame = Frame("Treeple",1000, 700, 360, 50)
        val menuFrame = Frame("Treeple Menu",300, 400, 50, 50)

        val panel = JPanel()
        panel.add(MenuClass())
        menuFrame.add(panel)
    }
}