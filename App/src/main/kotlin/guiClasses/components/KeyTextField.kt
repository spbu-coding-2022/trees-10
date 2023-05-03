package guiClasses.components

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