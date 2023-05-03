package guiClasses.components

import java.awt.Dimension
import javax.swing.ImageIcon
import javax.swing.JFrame

class Frame(name: String, width: Int, height: Int, locX: Int, locY: Int) : JFrame() {
    init {
        // Устанавливаем заголовок окна
        title = name

        // Устанавливаем размер окна
        setSize(width, height)
        minimumSize = Dimension(width, height)

        // Устанавливаем положение окна на экране
        setLocation(locX, locY)

        // Установка изображения
        val icon = ImageIcon("tree.jpg").image
        iconImage = icon

        // Устанавливаем операцию закрытия окна
        defaultCloseOperation = EXIT_ON_CLOSE

        // Отображаем окно
        isVisible = true
    }
}
