package com.eljavatar.swingutils.core.modelcomponents;


import java.io.Serializable;
import java.util.List;

/*
 * Copyright 2020 Andres.
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

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 * @param <T>
 */
public class ResponsePagination<T> implements Serializable {
    
    private Integer totalItems;
    private List<T> dataList;

    public ResponsePagination(Integer totalItems, List<T> dataList) {
        this.totalItems = totalItems;
        this.dataList = dataList;
    }

    public ResponsePagination() {
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
    
}
