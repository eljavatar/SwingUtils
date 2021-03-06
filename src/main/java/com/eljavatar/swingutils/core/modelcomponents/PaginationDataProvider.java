/*
 * Copyright 2018 ElJavatar.
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

package com.eljavatar.swingutils.core.modelcomponents;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.SortOrder;

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 * @param <T> Tipo de Objeto que tendran los elementos del JTable que usará el PaginatedTable
 */
public class PaginationDataProvider<T> extends JTableDataProvider<T> implements Serializable {
    
    private List<T> listDataFiltered;
    
    public PaginationDataProvider() {
        super();
        setListData(new ArrayList<>());
    }
    
    public PaginationDataProvider(List<T> listData) {
        super(listData);
    }

    public List<T> getListDataFiltered() {
        return listDataFiltered;
    }

    public void setListDataFiltered(List<T> listDataFiltered) {
        this.listDataFiltered = listDataFiltered;
    }
    
    @Override
    public List<T> getRows(int startIndex, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<T> listaFiltrada = null;
        
        if (!sortOrder.equals(SortOrder.UNSORTED) && sortField != null && !sortField.trim().isEmpty()) {
            getListData().sort((T o1, T o2) -> {
                try {
                    Class clazz = o1.getClass();
                    Field field = getFieldByNameFromSuperClasses(clazz, sortField);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), field.getDeclaringClass());
                    Method methodGetField = propertyDescriptor.getReadMethod();

                    Object valueData1 = methodGetField.invoke(o1);
                    Object valueData2 = methodGetField.invoke(o2);

                    if (valueData1 instanceof Comparable && valueData2 instanceof Comparable) {
                        if (sortOrder.equals(SortOrder.ASCENDING)) {
                            return ((Comparable )valueData1).compareTo((Comparable) valueData2);
                        } else {
                            return ((Comparable )valueData2).compareTo((Comparable) valueData1);
                        }
                    }
                    return 0;
                } catch (NoSuchFieldException | SecurityException | IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(PaginationDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IllegalArgumentException(ex);
                }
            });
        }
        
        for (Entry<String, Object> entry : filters.entrySet()) {
            String keyFilter = entry.getKey();
            ObjectFilter objectFilter = (ObjectFilter) entry.getValue();
            
            if (objectFilter != null && objectFilter.getValue() != null && !objectFilter.getValue().toString().trim().isEmpty()) {
                listaFiltrada = getListData().stream().map((objectData) -> {
                    try {
                        Class clazz = objectData.getClass();
                        Field field = getFieldByNameFromSuperClasses(clazz, keyFilter);
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), field.getDeclaringClass());
                        Method methodGetField = propertyDescriptor.getReadMethod();
                        
                        Object valueData = methodGetField.invoke(objectData);
                        
                        if (valueData != null && 
                                ((FilterMatchModeEnum.EXACT.equals(objectFilter.getFilterMatchModeEnum()) && valueData.equals(objectFilter.getValue())) 
                                || (FilterMatchModeEnum.STARTS_WITH.equals(objectFilter.getFilterMatchModeEnum()) && valueData.toString().startsWith(objectFilter.getValue().toString().trim())) 
                                || (FilterMatchModeEnum.ENDS_WITH.equals(objectFilter.getFilterMatchModeEnum()) && valueData.toString().endsWith(objectFilter.getValue().toString().trim())) 
                                || ((FilterMatchModeEnum.CONTAINS.equals(objectFilter.getFilterMatchModeEnum()) || objectFilter.getFilterMatchModeEnum() == null) && valueData.toString().matches(objectFilter.getValueWithPattern())))
                                ) {
                            return objectData;
                        } else {
                            return null;
                        }
                    } catch (NoSuchFieldException | SecurityException | IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(PaginationDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IllegalArgumentException(ex);
                    }       
                }).filter(objectData -> objectData != null).collect(Collectors.toList());
                
                break;
            }
        }

        setListDataFiltered(returnListaFiltrada(listaFiltrada, startIndex, pageSize));
        return getListDataFiltered();
    }
    
    public List<T> returnListaFiltrada(List<T> listaFiltrada, int startIndex, int pageSize) {
        int endIndex = startIndex + pageSize;
        if (listaFiltrada == null) {
            if (endIndex > getListData().size()) {
                endIndex = getListData().size();
            }
            setRowCount(getListData().size());
            return getListData().subList(startIndex, endIndex);
        } else {
            setRowCount(listaFiltrada.size());
            if (endIndex > listaFiltrada.size()) {
                endIndex = listaFiltrada.size();
            }
            return listaFiltrada.subList(startIndex, endIndex);
        }
    }
    
    private Field getFieldByNameFromSuperClasses(Class<? extends Object> clazz, String nameField) throws NoSuchFieldException, SecurityException {
        try {
            return clazz.getDeclaredField(nameField);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() == null) {
                throw ex;
            } else {
                Class<?> superClass = clazz.getSuperclass();
                return getFieldByNameFromSuperClasses(superClass, nameField);
            }
        } catch (SecurityException ex) {
            throw ex;
        }
    }
    
}
