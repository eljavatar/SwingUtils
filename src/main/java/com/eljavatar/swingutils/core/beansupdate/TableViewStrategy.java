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

import com.eljavatar.swingutils.core.modelcomponents.TableModelGeneric;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import javax.swing.JTable;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class TableViewStrategy implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        if (params.getTypeComponentView() == JTable.class) {
            JTable jTable = (JTable) params.getObjectComponentView();
            TableModelGeneric model = (TableModelGeneric) jTable.getModel();
            if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.MODEL)) {
                //fieldModel.set(objectModelBean, model.getListElements());
                params.getMethodSetFieldModel().invoke(params.getObjectModelBean(), model.getListElements());
            }
            if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.VIEW)) {
                //model.setListElements((List) fieldModel.get(objectModelBean));
                model.setListElements((List) params.getMethodGetFieldModel().invoke(params.getObjectModelBean()));
                model.fireTableDataChanged();
            }
        } else {
            throw new IllegalArgumentException("Component in View " + params.getNameComponentView() + " no es de tipo JTable de java swing");
        }
    }
    
}
