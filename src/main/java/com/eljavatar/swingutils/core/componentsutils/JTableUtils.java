package com.eljavatar.swingutils.core.componentsutils;

import com.eljavatar.swingutils.core.renderercomponents.TitlesTableRendererDefault;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class JTableUtils {
    
    public static void setProperties(final JTable jTable, Font fontTitle, TableModel tableModel) {
        setEstilosForTitulosDefault(jTable, fontTitle);
        setPropiedadesGenericsForTable(jTable);
        jTable.setModel(tableModel);
    }
    
//    public static void setPropertiesWithFilter(final JTable jTable, Font fontTitle, Font fontFilter, TableModel tableModel, int... colwidth) {
//        setEstilosForTitulosWithFilter(jTable, fontTitle, fontFilter);
//        setPropiedadesGenericsForTable(jTable);
//        jTable.setModel(tableModel);
//        setAnchoColumnas(jTable, jTable.getWidth() - 1, jTable.getColumnCount(), colwidth);
//    }
    
    public static void setProperties(final JTable jTable, Font fontTitle, TableModel tableModel, int... colwidth) {
        setProperties(jTable, fontTitle, tableModel);
        setAnchoColumnas(jTable, jTable.getWidth() - 1, jTable.getColumnCount(), colwidth);
    }
    
    public static void setProperties(final JTable jTable, Font fontTitle, TableModel tableModel, int widthTable, int colcount, int... colwidth) {
        setProperties(jTable, fontTitle, tableModel);
        setAnchoColumnas(jTable, widthTable, colcount, colwidth);
    }
    
    public static void setEstilosForTitulosDefault(final JTable jTable, Font fontTitle) {
        JTableHeader th = jTable.getTableHeader();
        th.setFont(fontTitle);

        TableCellRenderer tcr = jTable.getTableHeader().getDefaultRenderer();
        TitlesTableRendererDefault renderer = new TitlesTableRendererDefault(tcr);
        th.setDefaultRenderer(renderer);
    }
    
//    public static void setEstilosForTitulosWithFilter(final JTable jTable, Font fontTitle, Font fontFilter) {
//        JTableHeader th = jTable.getTableHeader();
//        th.setFont(fontTitle);
//
//        TableCellRenderer tcr = jTable.getTableHeader().getDefaultRenderer();
//        TitlesTableRendererFilter renderer = new TitlesTableRendererFilter(tcr, fontFilter);
//        th.setFocusable(true);
//        th.setDefaultRenderer(renderer);
//    }
    
    public static void setPropiedadesGenericsForTable(final JTable jTable) {
        jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable.getTableHeader().setReorderingAllowed(false);
    }
    
    public static void setAnchoColumnas(final JTable jTable, int widthTable, int colcount, int... colwidth) {
        int[] anchoColumnas = getAnchoColumnas(widthTable, colcount, colwidth);
        TableColumn columnaTabla;
        for (int i = 0; i < colcount; i++) {
            columnaTabla = jTable.getColumnModel().getColumn(i);
            columnaTabla.setPreferredWidth(anchoColumnas[i]);
            columnaTabla.setMinWidth(anchoColumnas[i]);
            columnaTabla.setMaxWidth(anchoColumnas[i]);
        }
    }
    
    private static int[] getAnchoColumnas(int anchoTabla, int colcount, int... colwidth) {
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
    
}
