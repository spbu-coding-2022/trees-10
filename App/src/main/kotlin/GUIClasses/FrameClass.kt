package Frame

import javax.swing.*

class Frame(name: String, width: Int, heidht: Int, locX: Int, locY: Int) : JFrame() {
    init {
        // Устанавливаем заголовок окна
        title = name

        // Устанавливаем размер окна
        setSize(width, heidht)

        // Устанавливаем положение окна на экране
        setLocation(locX, locY)

        // Создаем панель с прокруткой
        val scrollPane = JScrollPane()

        // Установка изображения
        val icon = ImageIcon("tree.jpg").image
        iconImage = icon

        // Создаем панель с компонентами
        val panel = JPanel()

        // Добавляем панель на панель с прокруткой
        scrollPane.setViewportView(panel)

        // Добавляем панель с прокруткой на окно
        contentPane.add(scrollPane)

        // Устанавливаем режим прокрутки по вертикали и горизонтали
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        //Запрет на форматирование окна
        isResizable = false

        // Устанавливаем операцию закрытия окна
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        // Отображаем окно
        isVisible = true
    }
}
