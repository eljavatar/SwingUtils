package com.eljavatar.swingutils.core.modelcomponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <E> Tipo de Objeto que tendrán los elementos del JComboBox
 */
public class ComboBoxModelGeneric<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable {

    private List<E> listElements;
    private E selected;
    private boolean isSynchronized;
    private boolean needDetectConcurrentModifications;
    
    /**
     * 
     */
    public ComboBoxModelGeneric() {
        this(true);
    }
    
    /**
     * 
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto
     *      y no seleccionable al principio de la lista
     */
    public ComboBoxModelGeneric(boolean addElementNullDefault) {
        this(addElementNullDefault, false, true);
    }
    
    /**
     * 
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public ComboBoxModelGeneric(boolean isSynchronized, boolean needDetectConcurrentModifications) {
        this(true, isSynchronized, needDetectConcurrentModifications);
    }
    
    /**
     * 
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto
     *      y no seleccionable al principio de la lista
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public ComboBoxModelGeneric(boolean addElementNullDefault, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        this.isSynchronized = isSynchronized;
        this.needDetectConcurrentModifications = needDetectConcurrentModifications;
        
        if (this.isSynchronized) {
            this.listElements = this.needDetectConcurrentModifications ? Collections.synchronizedList(new ArrayList<>()) : new CopyOnWriteArrayList<>();
        } else {
            this.listElements = new ArrayList<>();
        }
        
        if (addElementNullDefault) {
            listElements.add(null);
        }
    }
    
    /**
     * 
     * @param list Lista de elementos que tendrá el Modelo del JComboBox
     */
    public ComboBoxModelGeneric(List<E> list) {
        this(list, true, false, true);
    }
    
    /**
     * 
     * @param list Lista de elementos que tendrá el Modelo del JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto
     *      y no seleccionable al principio de la lista
     */
    public ComboBoxModelGeneric(List<E> list, boolean addElementNullDefault) {
        this(list, addElementNullDefault, false, true);
    }
    
    /**
     * 
     * @param list Lista de elementos que tendrá el Modelo del JComboBox
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public ComboBoxModelGeneric(List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        this(list, true, isSynchronized, needDetectConcurrentModifications);
    }
    
    /**
     * 
     * @param list Lista de elementos que tendrá el Modelo del JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto
     *      y no seleccionable al principio de la lista
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public ComboBoxModelGeneric(List<E> list, boolean addElementNullDefault, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        this.isSynchronized = isSynchronized;
        this.needDetectConcurrentModifications = needDetectConcurrentModifications;
        
        if (this.isSynchronized) {
            this.listElements = this.needDetectConcurrentModifications ? Collections.synchronizedList(new ArrayList<>(list)) : new CopyOnWriteArrayList<>(list);
        } else {
            this.listElements = new ArrayList<>(list);
        }
        
        if (addElementNullDefault) {
            listElements.add(0, null);
        }
        if (listElements.size() > 0) {
            selected = listElements.get(0);
        }
    }

    @Override
    public int getSize() {
        return listElements.size();
    }

    @Override
    public E getElementAt(int index) {
        if (index >= 0 && index < listElements.size())
            return listElements.get(index);
        else
            return null;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if ((selected != null && !selected.equals(anItem)) || selected == null && anItem != null) {
            selected = (E) anItem;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public E getSelectedItem() {
        return selected;
    }

    public List<E> getListElements() {
        return listElements;
    }

    public void setListElements(List<E> listElements) {
        if (this.isSynchronized) {
            this.listElements = this.needDetectConcurrentModifications ? Collections.synchronizedList(new ArrayList<>(listElements)) : new CopyOnWriteArrayList<>(listElements);
        } else {
            this.listElements = new ArrayList<>(listElements);
        }
        fireIntervalAdded(this, this.listElements.size()-1, this.listElements.size()-1);
    }
    
    public void addAllElements(List<E> elements) {
        this.listElements.addAll(elements);
        fireIntervalAdded(this, listElements.size()-1, listElements.size()-1);
    }
    
    @Override
    public void addElement(E element) {
        this.listElements.add(element);
        fireIntervalAdded(this, listElements.size()-1, listElements.size()-1);
        if (listElements.size() == 1 && selected == null && element != null) {
            setSelectedItem(element);
        }
    }

    @Override
    public void removeElement(Object obj) {
        int index = listElements.indexOf(obj);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    @Override
    public void insertElementAt(E item, int index) {
        listElements.add(index, item);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public void removeElementAt(int index) {
        if (getElementAt(index) == selected) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        listElements.remove(index);

        fireIntervalRemoved(this, index, index);
    }
    
}
