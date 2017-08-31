package com.eljavatar.swingutils.core.componentsutils;

import com.eljavatar.swingutils.core.renderercomponents.TitlesTableRendererDefault;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class JTableUtils extends DefaultTableModel {
    
    public JTableUtils() {}
    
    public JTableUtils(final JTable jTable, Font fontTitle, String[] titles, int selectionMode, int autoResizeMode, boolean reorderingAllowed) {
        setEstilosForTitulos(jTable, fontTitle);
        jTable.setSelectionMode(selectionMode);
        jTable.setAutoResizeMode(autoResizeMode);
        jTable.getTableHeader().setReorderingAllowed(reorderingAllowed);
    }
    
    public JTableUtils(final JTable jTable, Font fontTitle, String[] titulos) {
        setEstilosForTitulos(jTable, fontTitle);
        setPropiedadesForTable(jTable);
    }
    
    public final void setEstilosForTitulos(final JTable jTable, Font fontTitle) {
        JTableHeader th = jTable.getTableHeader();
        th.setFont(fontTitle);

        TableCellRenderer tcr = jTable.getTableHeader().getDefaultRenderer();
        TitlesTableRendererDefault editortitulos = new TitlesTableRendererDefault(tcr);
        th.setDefaultRenderer(editortitulos);
    }
    
    public final void setPropiedadesForTable(final JTable jTable) {
        jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable.getTableHeader().setReorderingAllowed(false);
    }
    
    public void setAnchoColumnas(JTable jTable, int widthTable, int colcount, int... colwidth) {
        int[] anchoColumnas = getAnchoColumnas(widthTable, colcount, colwidth);
        TableColumn columnaTabla;
        for (int i = 0; i < colcount; i++) {
            columnaTabla = jTable.getColumnModel().getColumn(i);
            columnaTabla.setPreferredWidth(anchoColumnas[i]);
            columnaTabla.setMinWidth(anchoColumnas[i]);
            columnaTabla.setMaxWidth(anchoColumnas[i]);
        }
    }
    
    private int[] getAnchoColumnas(int anchoTabla, int colcount, int... colwidth) {
        int[] anchoColumnas = new int[colcount];
        for (int i = 0; i < colcount; i++) {
            if (colwidth[i] == 0) {
                anchoColumnas[i] = 0;
            } else {
                anchoColumnas[i] = (colwidth[i]) * anchoTabla / 100;
            }
        }
        return anchoColumnas;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
}
