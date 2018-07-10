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
package com.eljavatar.swingutils.core.modelcomponents;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.swing.SortOrder;

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 * @param <T> Tipo de Objeto que tendran los elementos del JTable que usará el PaginatedTable
 */
public abstract class JTableDataProvider<T> implements Serializable {
    
    /**
     * Lista de elementos
     */
    private List<T> listData;

    /**
     * Cantidad de filas
     */
    private int rowCount;

    public JTableDataProvider() {
    }
    
    public JTableDataProvider(List<T> listData) {
        this.listData = listData;
    }

    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
    public List<T> getRows(int startIndex, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        throw new UnsupportedOperationException("Rows loading is not implemented.");
    }
    
}
