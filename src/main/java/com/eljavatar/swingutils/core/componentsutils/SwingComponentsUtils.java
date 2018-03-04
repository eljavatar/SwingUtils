/*
 * Copyright 2018 Andres.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eljavatar.swingutils.core.componentsutils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.prompt.PromptSupport;

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
    
    public static void mostrarInfo(Component parent, String mensaje, String titulo) {
        mostrarMensaje(parent, mensaje, titulo, INFO);
    }
    
    public static void mostrarWarning(Component parent, String mensaje, String titulo) {
        mostrarMensaje(parent, mensaje, titulo, WARNING);
    }
    
    public static void mostrarError(Component parent, String mensaje, String titulo) {
        mostrarMensaje(parent, mensaje, titulo, ERROR);
    }
    
    public static void mostrarQuestion(Component parent, String mensaje, String titulo) {
        mostrarMensaje(parent, mensaje, titulo, QUESTION);
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
    
    public static void setPlaceholder(String placeholder, JTextComponent textComponent) {
        PromptSupport.setPrompt(placeholder, textComponent);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, textComponent);
        PromptSupport.setFontStyle(Font.ITALIC, textComponent);
        PromptSupport.setForeground(Color.GRAY, textComponent);
    }
    
}
