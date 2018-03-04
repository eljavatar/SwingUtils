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
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     * @param fontTitle Font que tendrá el header del JTable
     * @param tableModel Modelo de datos que usará el JTable
     */
    public static void setProperties(final JTable jTable, Font fontTitle, TableModel tableModel) {
        setProperties(jTable, fontTitle, tableModel, javax.swing.ListSelectionModel.SINGLE_SELECTION, javax.swing.JTable.AUTO_RESIZE_OFF, false);
    }
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     * @param fontTitle Font que tendrá el header del JTable
     * @param tableModel Modelo de datos que usará el JTable
     * @param selectionMode Modo de selección de filas en el JTable
     * @param autoresizeMode Modo en el que se permitirá el cambio de tamaño (ancho) de columnas
     * @param reorderingAllowed Indica si se permitirá cambiar el orden en el que están las columnas
     */
    public static void setProperties(final JTable jTable, Font fontTitle, TableModel tableModel, int selectionMode, int autoresizeMode, boolean reorderingAllowed) {
        setEstilosForTitulosDefault(jTable, fontTitle);
        setPropiedadesGenericsForTable(jTable, selectionMode, autoresizeMode, reorderingAllowed);
        jTable.setModel(tableModel);
    }
    
//    public static void setPropertiesWithFilter(final JTable jTable, Font fontTitle, Font fontFilter, TableModel tableModel, int... colwidth) {
//        setEstilosForTitulosWithFilter(jTable, fontTitle, fontFilter);
//        setPropiedadesGenericsForTable(jTable);
//        jTable.setModel(tableModel);
//        setAnchoColumnas(jTable, jTable.getWidth() - 1, jTable.getColumnCount(), colwidth);
//    }
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     * @param fontTitle Font que tendrá el header del JTable
     * @param tableModel Modelo de datos que usará el JTable
     * @param colwidth Array de enteros con el tamaño en porcentaje que tendrá cada una de las columnas (La sumatoria de todos no debería pasar de 100)
     */
    public static void setProperties(final JTable jTable, Font fontTitle, TableModel tableModel, int... colwidth) {
        setProperties(jTable, fontTitle, tableModel, javax.swing.ListSelectionModel.SINGLE_SELECTION, javax.swing.JTable.AUTO_RESIZE_OFF, false, colwidth);
    }
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     * @param fontTitle Font que tendrá el header del JTable
     * @param tableModel Modelo de datos que usará el JTable
     * @param selectionMode Modo de selección de filas en el JTable
     * @param autoresizeMode Modo en el que se permitirá el cambio de tamaño (ancho) de columnas
     * @param reorderingAllowed Indica si se permitirá cambiar el orden en el que están las columnas
     * @param colwidth Array de enteros con el tamaño en porcentaje que tendrá cada una de las columnas (La sumatoria de todos no debería pasar de 100)
     */
    public static void setProperties(final JTable jTable, Font fontTitle, TableModel tableModel, int selectionMode, int autoresizeMode, boolean reorderingAllowed, int... colwidth) {
        setProperties(jTable, fontTitle, tableModel, selectionMode, autoresizeMode, reorderingAllowed);
        setAnchoColumnas(jTable, jTable.getWidth() - 1, jTable.getColumnCount(), colwidth);
    }
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     * @param fontTitle Font que tendrá el header del JTable
     * @param tableModel Modelo de datos que usará el JTable
     * @param widthTable Ancho del JTable
     * @param colcount Cantidad de columnas del JTable
     * @param colwidth Array de enteros con el tamaño en porcentaje que tendrá cada una de las columnas (La sumatoria de todos no debería pasar de 100)
     * @deprecated 
     */
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
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     */
    public static void setPropiedadesGenericsForTable(final JTable jTable) {
        jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable.getTableHeader().setReorderingAllowed(false);
    }
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     * @param selectionMode Modo de selección de filas en el JTable
     * @param autoresizeMode Modo en el que se permitirá el cambio de tamaño (ancho) de columnas
     * @param reorderingAllowed Indica si se permitirá cambiar el orden en el que están las columnas
     */
    public static void setPropiedadesGenericsForTable(final JTable jTable, int selectionMode, int autoresizeMode, boolean reorderingAllowed) {
        jTable.setSelectionMode(selectionMode);
        jTable.setAutoResizeMode(autoresizeMode);
        jTable.getTableHeader().setReorderingAllowed(reorderingAllowed);
    }
    
    /**
     * 
     * @param jTable JTable al cual queremos aplicar las propiedades
     * @param widthTable Ancho del JTable
     * @param colcount Cantidad de columnas del JTable
     * @param colwidth Array de enteros con el tamaño en porcentaje que tendrá cada una de las columnas (La sumatoria de todos no debería pasar de 100)
     */
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
    
    /**
     * 
     * @param widthTable Ancho del JTable
     * @param colcount Cantidad de columnas del JTable
     * @param colwidth Array de enteros con el tamaño en porcentaje que tendrá cada una de las columnas (La sumatoria de todos no debería pasar de 100)
     * @return Retorna un array con los tamaños que tendrán cada una de las columnas del JTable basados en su porcentaje asignado
     */
    private static int[] getAnchoColumnas(int widthTable, int colcount, int... colwidth) {
        int[] anchoColumnas = new int[colcount];
        for (int i = 0; i < colcount; i++) {
            if (colwidth[i] == 0) {
                anchoColumnas[i] = 0;
            } else {
                anchoColumnas[i] = (colwidth[i]) * widthTable / 100;
            }
        }
        return anchoColumnas;
    }
    
}
