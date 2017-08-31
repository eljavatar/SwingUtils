package com.eljavatar.swingutils.core.componentsutils;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class SwingComponentsUtils {

    public static final int WARNING = JOptionPane.WARNING_MESSAGE;
    public static final int INFO = JOptionPane.INFORMATION_MESSAGE;
    public static final int QUESTION = JOptionPane.QUESTION_MESSAGE;
    public static final int ERROR = JOptionPane.ERROR_MESSAGE;
    
    public static void mostrarMensaje(Component parent, String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(parent, mensaje, titulo, tipo);
    }
    
    public static void enabledComponents(JComponent component, boolean enabled) {
        for (Component c : component.getComponents()) {
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                if (jc.getComponents().length > 0) {
                    enabledComponents(jc, enabled);
                }
            }
            c.setEnabled(enabled);
        }
        component.setEnabled(enabled);
    }
    
    public static void editableComponents(JComponent component, boolean editable) {
        for (Component c : component.getComponents()) {
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                if (jc.getComponents().length > 0) {
                    editableComponents(jc, editable);
                }
            }
            if (c instanceof JComboBox) {
                JComboBox combo = (JComboBox) c;
                combo.setEnabled(editable);
            }
            if (c instanceof JTextComponent) {
                JTextComponent text = (JTextComponent) c;
                text.setEditable(editable);
            }
        }
    }
    
    public static void setFontAllComponents(JComponent component, Font font) {
        for (Component c : component.getComponents()) {
            if (c instanceof JComponent) {
                JComponent jc = (JComponent) c;
                if (jc.getComponents().length > 0) {
                    setFontAllComponents(jc, font);
                }
            }
            c.setFont(font);
        }
        component.setFont(font);
    }
    
    public static Frame convertComponentToFrame(Component component) {
        Window parentWindow = SwingUtilities.windowForComponent(component);
        Frame parentFrame = null;
        if (parentWindow instanceof Frame) {
            parentFrame = (Frame) parentWindow;
        }
        return parentFrame;
    }
    
}
