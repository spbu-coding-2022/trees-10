package guiClasses.components

import javax.swing.JButton
import javax.swing.JTextField

class KeyTextField(
    private val button: JButton
) : JTextField() {

    private val textField = JTextField("Key")
    init {
        textField.also {
            addActionListener {
                button.doClick()
            }
        }

        add(textField)

    }
}