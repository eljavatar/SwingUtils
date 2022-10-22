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
package com.eljavatar.swingutils.core.beansupdate;

import com.eljavatar.swingutils.core.components.PaginatedTable;
import com.eljavatar.swingutils.core.modelcomponents.PaginationDataProvider;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class PaginatedTableViewStrategy implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        if (params.getTypeComponentView() == PaginatedTable.class) {
            PaginatedTable paginatedTable = (PaginatedTable) params.getObjectComponentView();
            if (!paginatedTable.isLazy()) {
                PaginationDataProvider dataProvider = paginatedTable.getPaginationDataProvider();
                if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.MODEL)) {
                    params.getMethodSetFieldModel().invoke(params.getObjectModelBean(), dataProvider.getListData());
                }
                if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.VIEW)) {
                    List listData = (List) params.getMethodGetFieldModel().invoke(params.getObjectModelBean());
                    dataProvider.setListData(listData);
                    dataProvider.setRowCount(listData.size());
                    paginatedTable.resetPaginatedTable();
                }
            }
        } else {
            throw new IllegalArgumentException("Component in View " + params.getNameComponentView() + " no es de tipo PaginatedTable de SwingUtils");
        }
    }
    
}
