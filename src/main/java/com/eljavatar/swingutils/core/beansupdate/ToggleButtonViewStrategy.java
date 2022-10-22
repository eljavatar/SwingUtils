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

import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import javax.swing.JToggleButton;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ToggleButtonViewStrategy implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        if (JToggleButton.class.isAssignableFrom(params.getTypeComponentView())) {
            JToggleButton jToggleButton = (JToggleButton) params.getObjectComponentView();
            if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.MODEL)) {
                //fieldModel.set(objectModelBean, jToggleButton.isSelected());
                params.getMethodSetFieldModel().invoke(params.getObjectModelBean(), jToggleButton.isSelected());
            }
            if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.VIEW)) {
                if (params.getTypeFieldModel() == Boolean.class || params.getTypeFieldModel() == boolean.class) {
                    //Boolean bool = (Boolean) fieldModel.get(objectModelBean);
                    Boolean bool = (Boolean) params.getMethodGetFieldModel().invoke(params.getObjectModelBean());
                    jToggleButton.setSelected(bool != null ? bool : Boolean.FALSE);
                    for (ActionListener actionListener : jToggleButton.getActionListeners()) {
                        actionListener.actionPerformed(null);
                    }
                } else {
                    throw new IllegalArgumentException("Atributo in Model " + params.getFieldView().getName() + " no es de tipo boolean");
                }
            }

        } else {
            throw new IllegalArgumentException("Component in View " + params.getNameComponentView() + " no es de tipo JToggleButton de java swing");
        }
    }
    
}
