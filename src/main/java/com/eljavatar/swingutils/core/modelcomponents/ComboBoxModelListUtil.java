package com.eljavatar.swingutils.core.modelcomponents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <E> Tipo de Objeto que tendrán los elementos del JComboBox
 */
public class ComboBoxModelListUtil<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable {

    private List<E> listElements;
    private E selected;

    public ComboBoxModelListUtil() {
        this(true);
    }
    
    public ComboBoxModelListUtil(boolean addElementNullDefault) {
        this.listElements = new ArrayList<>();
        if (addElementNullDefault) {
            listElements.add(null);
        }
    }
    
    public ComboBoxModelListUtil(List<E> list) {
        this(list, true);
    }
    
    public ComboBoxModelListUtil(List<E> list, boolean addElementNullDefault) {
        this.listElements = new ArrayList<>(list);
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
        this.listElements = listElements;
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
