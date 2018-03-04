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

import java.io.Serializable;

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 */
public class ObjectFilter implements Serializable {

    private String nameIntoComboBox;
    private String nameFilter;
    private int column;
    private FilterMatchModeEnum filterMatchModeEnum;
    private Object value;
    private String valueWithPattern;

    public ObjectFilter(String nameIntoComboBox, String nameFilter, int column, FilterMatchModeEnum filterMatchModeEnum) {
        this.nameIntoComboBox = nameIntoComboBox;
        this.nameFilter = nameFilter;
        this.column = column;
        this.filterMatchModeEnum = filterMatchModeEnum;
    }
    
    public ObjectFilter(String nameIntoComboBox, String nameFilter, FilterMatchModeEnum filterMatchModeEnum) {
        this.nameIntoComboBox = nameIntoComboBox;
        this.nameFilter = nameFilter;
        this.filterMatchModeEnum = filterMatchModeEnum;
    }

    public ObjectFilter(String nameIntoComboBox, String nameFilter, int column) {
        this.nameIntoComboBox = nameIntoComboBox;
        this.nameFilter = nameFilter;
        this.column = column;
    }

    public ObjectFilter(String nameIntoComboBox, String nameFilter) {
        this.nameIntoComboBox = nameIntoComboBox;
        this.nameFilter = nameFilter;
    }

    public ObjectFilter() {
    }

    public String getNameIntoComboBox() {
        return nameIntoComboBox;
    }

    public void setNameIntoComboBox(String nameIntoComboBox) {
        this.nameIntoComboBox = nameIntoComboBox;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public FilterMatchModeEnum getFilterMatchModeEnum() {
        return filterMatchModeEnum;
    }

    public void setFilterMatchModeEnum(FilterMatchModeEnum filterMatchModeEnum) {
        this.filterMatchModeEnum = filterMatchModeEnum;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getValueWithPattern() {
        return valueWithPattern;
    }

    public void setValueWithPattern(String valueWithPattern) {
        this.valueWithPattern = valueWithPattern;
    }
    
}
