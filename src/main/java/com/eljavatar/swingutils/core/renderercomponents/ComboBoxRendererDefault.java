package com.eljavatar.swingutils.core.renderercomponents;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ComboBoxRendererDefault extends BasicComboBoxRenderer {

    private final String textNoSeleccionable;
    private final ListCellRenderer rendererDefault;
    
    public ComboBoxRendererDefault(ListCellRenderer rendererDefault, String textNoSeleccionable) {
        super();
        this.rendererDefault = rendererDefault;
        this.textNoSeleccionable = textNoSeleccionable;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        javax.swing.JComponent component = (javax.swing.JComponent) rendererDefault.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        javax.swing.JLabel label = (javax.swing.JLabel) component;
        
        label.setText((value == null) ? textNoSeleccionable : value.toString());

        return label;
    }

}
