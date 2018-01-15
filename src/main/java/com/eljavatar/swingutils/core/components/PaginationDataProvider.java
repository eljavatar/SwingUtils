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

package com.eljavatar.swingutils.core.components;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 * @param <T>
 */
public abstract class PaginationDataProvider<T> implements Serializable {

    private int totalRowCount;
    
    private List<T> listData;
    
    private List<T> listDataFiltered;
    
    public int getTotalRowCount() {
        return totalRowCount;
    }
    
    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    public List<T> getListDataFiltered() {
        return listDataFiltered;
    }

    public void setListDataFiltered(List<T> listDataFiltered) {
        this.listDataFiltered = listDataFiltered;
    }
    
    public List<T> getRows(int startIndex, int endIndex, Map<String, Object> filters) {
        throw new UnsupportedOperationException("Rows loading is not implemented.");
    }
    
    public void resetListData() {
        throw new UnsupportedOperationException("Rows reset is not implemented.");
    }
    
}
