package com.eljavatar.swingutils.core.renderercomponents;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ComboBoxRendererDefault extends BasicComboBoxRenderer implements JComboBox.KeySelectionManager {

    private final String textNoSeleccionable;
    private final ListCellRenderer rendererDefault;
    private Method method;

    private long timeFactor;
    private long lastTime;
    private long currentTime;
    private String prefix = "";

    public ComboBoxRendererDefault(JComboBox jComboBox, ListCellRenderer rendererDefault, String textNoSeleccionable, Class clazz, String nameMethod) {
        super();
        this.rendererDefault = rendererDefault;
        this.textNoSeleccionable = textNoSeleccionable;

        jComboBox.setRenderer(this);
        jComboBox.setKeySelectionManager(this);

        Long value = (Long) UIManager.get("ComboBox.timeFactor");
        timeFactor = value == null ? 250L : value;

        if (clazz != null && nameMethod != null) {
            try {
                this.method = clazz.getMethod(nameMethod);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(ComboBoxRendererDefault.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.method = null;
        }

    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        javax.swing.JComponent component = (javax.swing.JComponent) rendererDefault.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        javax.swing.JLabel label = (javax.swing.JLabel) component;

        label.setText(getDisplayValue(value));

        return label;
    }

    @Override
    public int selectionForKey(char aKey, ComboBoxModel model) {
        this.currentTime = System.currentTimeMillis();

        //  Get the index of the currently selected item
        int size = model.getSize();
        int startIndex = -1;
        Object selectedItem = model.getSelectedItem();

        if (selectedItem != null) {
            for (int i = 0; i < size; i++) {
                if (selectedItem == model.getElementAt(i)) {
                    startIndex = i;
                    break;
                }
            }
        }

        //  Determine the "prefix" to be used when searching the model. The
        //  prefix can be a single letter or multiple letters depending on how
        //  fast the user has been typing and on which letter has been typed.
        if (currentTime - lastTime < timeFactor) {
            if ((prefix.length() == 1) && (aKey == prefix.charAt(0))) {
                // Subsequent same key presses move the keyboard focus to the next
                // object that starts with the same letter.
                startIndex++;
            } else {
                prefix += aKey;
            }
        } else {
            startIndex++;
            prefix = "" + aKey;
        }

        lastTime = currentTime;

        //  Search from the current selection and wrap when no match is found
        if (startIndex < 0 || startIndex >= size) {
            startIndex = 0;
        }

        int index = getNextMatch(prefix, startIndex, size, model);

        if (index < 0) {
            // wrap
            index = getNextMatch(prefix, 0, startIndex, model);
        }

        return index;
    }

    private int getNextMatch(String prefix, int start, int end, ComboBoxModel model) {
        for (int i = start; i < end; i++) {
            Object item = model.getElementAt(i);
            if (item != null) {
                String displayValue = getDisplayValue(item).toLowerCase();
                if (displayValue.startsWith(prefix)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * This method must be implemented in the extended class.
     *
     * @param value an item from the ComboBoxModel
     * @return a String containing the text to be rendered for this item.
     */
    public String getDisplayValue(Object value) {
        String text;
        if (method != null && value != null) {
            try {
                text = (String) method.invoke(value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                text = value.toString();
                Logger.getLogger(ComboBoxRendererDefault.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            text = (value == null) ? textNoSeleccionable : value.toString();
        }
        return text;
    };

}
