package com.eljavatar.swingutils.core.renderercomponents;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class TitlesTableRendererDefault implements javax.swing.table.TableCellRenderer {

    private final TableCellRenderer rendererDefault;

    public TitlesTableRendererDefault(TableCellRenderer rendererDefault) {
        this.rendererDefault = rendererDefault;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        javax.swing.JComponent component = (javax.swing.JComponent) rendererDefault.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        javax.swing.JLabel label = (javax.swing.JLabel) component;
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        return label;
    }
    
}
