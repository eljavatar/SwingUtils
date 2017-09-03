package com.eljavatar.swingutils.core.modelcomponents;

import java.io.Serializable;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <E> Tipo de Objeto que tendran los elementos del JTable
 */
public class TableModelGeneric<E> extends AbstractTableModel implements Serializable {

    private final Class[] typeColumns;
    private final String[] titleColumns;
    private List<E> listElements;
    
    public TableModelGeneric(Class[] typeColumns, String[] titleColumns, List<E> listElements) {
        this.typeColumns = typeColumns;
        this.titleColumns = titleColumns;
        this.listElements = listElements;
    }
    
    @Override
    public String getColumnName(int column) {
        return titleColumns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return typeColumns[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public int getRowCount() {
        return listElements.size();
    }

    @Override
    public int getColumnCount() {
        return titleColumns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<E> getListElements() {
        return listElements;
    }

    public void setListElements(List<E> listElements) {
        this.listElements = listElements;
    }
    
    public void addElement(E element) {
        this.listElements.add(element);
    }
    
    public void addAllElements(List<E> elements) {
        this.listElements.addAll(elements);
    }
    
}
