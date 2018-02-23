package com.eljavatar.swingutils.core.modelcomponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <E> Tipo de Objeto que tendran los elementos del JTable
 */
public abstract class TableModelGeneric<E> extends AbstractTableModel implements Serializable {

    private final Class[] typeColumns;
    private final String[] titleColumns;
    private List<ObjectFilter> filtersProperties;
    private List<E> listElements;
    private boolean isSynchronized;
    private boolean needDetectConcurrentModifications;
    
    /**
     * 
     * @param typeColumns Tipos de datos de cada columna del JTable
     * @param titleColumns Títulos de las columnas del JTable
     * @param listElements Lista de elementos que tendrá el Modelo del JTable
     */
    public TableModelGeneric(Class[] typeColumns, String[] titleColumns, List<E> listElements) {
        this(typeColumns, titleColumns, listElements, false, true);
    }
    
    /**
     * 
     * @param typeColumns Tipos de datos de cada columna del JTable
     * @param titleColumns Títulos de las columnas del JTable
     * @param filtersProperties Nombres que tendrán los filtros de las columnas del JTable
     * @param listElements Lista de elementos que tendrá el Modelo del JTable
     */
    public TableModelGeneric(Class[] typeColumns, String[] titleColumns, List<ObjectFilter> filtersProperties, List<E> listElements) {
        this(typeColumns, titleColumns, filtersProperties, listElements, false, true);
    }
    
    /**
     * 
     * @param typeColumns Tipos de datos de cada columna del JTable
     * @param titleColumns Títulos de las columnas del JTable
     * @param listElements Lista de elementos que tendrá el Modelo del JTable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public TableModelGeneric(Class[] typeColumns, String[] titleColumns, List<E> listElements, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        this(typeColumns, titleColumns, new ArrayList<>(), listElements, false, true);
    }
    
    /**
     * 
     * @param typeColumns Tipos de datos de cada columna del JTable
     * @param titleColumns Títulos de las columnas del JTable
     * @param filtersProperties Nombres que tendrán los filtros de las columnas del JTable
     * @param listElements Lista de elementos que tendrá el Modelo del JTable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public TableModelGeneric(Class[] typeColumns, String[] titleColumns, List<ObjectFilter> filtersProperties, List<E> listElements, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        this.typeColumns = typeColumns;
        this.titleColumns = titleColumns;
        this.filtersProperties = filtersProperties;
        if (this.isSynchronized) {
            this.listElements = this.needDetectConcurrentModifications ? Collections.synchronizedList(listElements) : new CopyOnWriteArrayList<>(listElements);
        } else {
            this.listElements = listElements;
        }
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

    public List<E> getListElements() {
        return listElements;
    }

    public void setListElements(List<E> listElements) {
        if (this.isSynchronized) {
            this.listElements = this.needDetectConcurrentModifications ? Collections.synchronizedList(listElements) : new CopyOnWriteArrayList<>(listElements);
        } else {
            this.listElements = listElements;
        }
    }
    
    public void addElement(E element) {
        this.listElements.add(element);
    }
    
    public void addAllElements(List<E> elements) {
        this.listElements.addAll(elements);
    }
    
    public void addRow(E element) {
        addElement(element);
        fireTableRowsInserted(this.listElements.size(), this.listElements.size());
    }
    
    public void removeRow(int index) {
        this.listElements.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public Class[] getTypeColumns() {
        return typeColumns;
    }

    public String[] getTitleColumns() {
        return titleColumns;
    }

    public List<ObjectFilter> getFiltersProperties() {
        return filtersProperties;
    }
    
}
