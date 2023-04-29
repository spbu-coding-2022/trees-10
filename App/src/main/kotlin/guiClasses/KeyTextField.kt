package guiClasses

import java.awt.Font
import javax.swing.JTextField

class KeyTextField() : JTextField() {

    private val textField = JTextField("Key")
    init {
        textField.columns = 6
        textField.toolTipText = "key"

        add(textField)

    }
}