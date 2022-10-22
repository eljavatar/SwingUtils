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

import com.eljavatar.swingutils.core.annotations.ComboBoxView;
import com.eljavatar.swingutils.core.componentsutils.NotifyUtils;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ComboBoxViewStrategy implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        if (params.getTypeComponentView() == JComboBox.class) {
            ComboBoxView annotationComboBoxView = params.getFieldView().getAnnotation(ComboBoxView.class);
            JComboBox jCBcombo = (JComboBox) params.getObjectComponentView();
            boolean required = annotationComboBoxView.required();
            String requiredMessage = (annotationComboBoxView.requiredMessage() != null && !annotationComboBoxView.requiredMessage().isEmpty()) ? annotationComboBoxView.requiredMessage() : "El campo '" + params.getNameComponentView() + "' debe ser obligatorio";
            
            ComboBoxModel model = jCBcombo.getModel();
            if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.MODEL)) {
                //fieldModel.set(objectModelBean, model.getSelectedItem());
                Object value = model.getSelectedItem();
                //params.getMethodSetFieldModel().invoke(params.getObjectModelBean(), value);
                if (value == null && required) {
                    NotifyUtils.showErrorAutoHide(null, "Valor Requerido", requiredMessage);
                    //this.failedValidation = true;
                    params.setFailedValidation(true);
                } else {
                    params.getMethodSetFieldModel().invoke(params.getObjectModelBean(), value);
                }
            }
            if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.VIEW)) {
                //model.setSelectedItem(fieldModel.get(objectModelBean));
                model.setSelectedItem(params.getMethodGetFieldModel().invoke(params.getObjectModelBean()));
            }
        } else {
            throw new IllegalArgumentException("Component in View " + params.getNameComponentView() + " no es de tipo JComboBox de java swing");
        }
    }
    
}
