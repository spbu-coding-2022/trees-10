package guiClasses

import java.awt.Font
import java.awt.event.ActionListener
import javax.swing.JTextField

class KeyTextField() : JTextField() {

    private val textField = JTextField("Key")
    init {
        textField.also {
            addActionListener {
                text = "" }
        }

        add(textField)

    }
}