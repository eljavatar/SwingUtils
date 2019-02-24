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
 * Esta clase define el tipo de proveedor que deben usar los componentes de tipo {@link com.eljavatar.swingutils.core.components.PaginatedTable}
 * <p>
 * Esta clase está declarada como abstracta, ya que este tipo de componentes (los PaginatedTable)
 * no lo usan directamente, así que deben usar uno de sus dos tipos según sea la necesidad:
 * <ul>
 * <li>
 * {@link com.eljavatar.swingutils.core.modelcomponents.PaginationDataProvider}
 * <li>
 * {@link com.eljavatar.swingutils.core.modelcomponents.LazyDataProvider}
 * </ul>
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
    
    /**
     * <p>
     * 'Método que devuelve las filas que serán mostradas en un componente de tipo {@link com.eljavatar.swingutils.core.components.PaginatedTable}
     * <p>
     * Este método que debe ser implementado en las clases que hereden de esta; éste método se
     * implementado en esta clase lanzando una excepción de tipo {@link java.lang.UnsupportedOperationException}
     * ya que debe ser sobrescrito en las clases que hereden de ésta, puesto que según sea
     * la implementación (Paginated o Lazy), la forma de obtener las filas se realiza de forma distinta
     * @param startIndex Primera posición de la página actual que se muestra en el PaginatedTable
     * @param pageSize Cantidad de filas que se muestran por página
     * @param sortField Nombre de la columna por la cual se desean ordenar las filas
     * @param sortOrder Tipo de orden a aplicar para el ordenamiento de las filas
     * @param filters Mapa con los filtros a aplicar para el filtrado de la información en la tabla
     * @return Lista de filas a mostrar en un PaginatedTable
     */
    public List<T> getRows(int startIndex, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        throw new UnsupportedOperationException("Rows loading is not implemented.");
    }
    
}
