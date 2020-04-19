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
package com.eljavatar.swingutils.core.renderercomponents;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
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
    private Method methodLabel;
    private Method methodTooltip;
    
    private Function<Object, String> functionLabel;
    private Function<Object, String> functionTooltip;

    private long timeFactor;
    private long lastTime;
    private long currentTime;
    private String prefix = "";
    
    
    private ComboBoxRendererDefault(JComboBox jComboBox, ListCellRenderer rendererDefault, String textNoSeleccionable) {
        super();
        this.rendererDefault = rendererDefault;
        this.textNoSeleccionable = textNoSeleccionable;

        jComboBox.setRenderer(this);
        jComboBox.setKeySelectionManager(this);

        Long value = (Long) UIManager.get("ComboBox.timeFactor");
        timeFactor = value == null ? 250L : value;
    }

    public ComboBoxRendererDefault(JComboBox jComboBox, ListCellRenderer rendererDefault, String textNoSeleccionable, Class clazz, String nameMethodLabel, String nameMethodTooltip) {
        this(jComboBox, rendererDefault, textNoSeleccionable);
        
        if (clazz != null) {
            try {
                this.methodLabel = nameMethodLabel != null ? clazz.getMethod(nameMethodLabel) : null;
                this.methodTooltip = nameMethodTooltip != null ? clazz.getMethod(nameMethodTooltip) : null;
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(ComboBoxRendererDefault.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.methodLabel = null;
            this.methodTooltip = null;
        }

    }
    
    public ComboBoxRendererDefault(JComboBox jComboBox, ListCellRenderer rendererDefault, String textNoSeleccionable, Function functionLabel, Function functionTooltip) {
        this(jComboBox, rendererDefault, textNoSeleccionable);
        
        this.functionLabel = functionLabel != null ? functionLabel : null;
        this.functionTooltip = functionTooltip != null ? functionTooltip : null;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        javax.swing.JComponent component = (javax.swing.JComponent) rendererDefault.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        javax.swing.JLabel label = (javax.swing.JLabel) component;

        label.setText(getDisplayValue(value));
        label.setToolTipText(getTooltipValue(value));

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
     * @param valueItem an item from the ComboBoxModel
     * @return a String containing the text to be rendered for this item.
     */
    public String getDisplayValue(Object valueItem) {
        String text;
        if (methodLabel != null && valueItem != null) {
            try {
                text = (String) methodLabel.invoke(valueItem);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                text = valueItem.toString();
                Logger.getLogger(ComboBoxRendererDefault.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (functionLabel != null && valueItem != null) {
            text = functionLabel.apply(valueItem);
        } else {
            text = (valueItem == null) ? textNoSeleccionable : valueItem.toString();
        }
        return text;
    };
    
    /**
     * 
     * @param valueItem an item from the ComboBoxModel
     * @return a String containing the text to be rendered for the tooltip
     */
    public String getTooltipValue(Object valueItem) {
        String text;
        if (methodTooltip != null && valueItem != null) {
            try {
                text = (String) methodTooltip.invoke(valueItem);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                text = valueItem.toString();
                Logger.getLogger(ComboBoxRendererDefault.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (functionTooltip != null && valueItem != null) {
            text = functionTooltip.apply(valueItem);
        } else {
            text = null;
        }
        return text;
    };

}
