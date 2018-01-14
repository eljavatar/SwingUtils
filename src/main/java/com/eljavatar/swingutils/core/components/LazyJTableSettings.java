/*
 * Copyright 2017 ElJavatar.
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

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 */
public class LazyJTableSettings {

    private boolean addGlobalFilter;
    private boolean lazy;
    private int sizeData;
    private int currentPage;
    private int pageSize;
    
    public LazyJTableSettings() {
        this.addGlobalFilter = true;
        this.lazy = false;
    }

    public boolean isAddGlobalFilter() {
        return addGlobalFilter;
    }

    public void setAddGlobalFilter(boolean addGlobalFilter) {
        this.addGlobalFilter = addGlobalFilter;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public int getSizeData() {
        return sizeData;
    }

    public void setSizeData(int sizeData) {
        this.sizeData = sizeData;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
}
