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

import com.eljavatar.swingutils.core.annotations.ComponentView;
import com.eljavatar.swingutils.core.componentsutils.NotifyUtils;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ComponentViewStrategy implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        ComponentView annotationComponentView = params.getFieldView().getAnnotation(ComponentView.class);
        String nameProperty = annotationComponentView.nameProperty().trim();
        
        boolean required = annotationComponentView.required();
        String requiredMessage = (annotationComponentView.requiredMessage() != null && !annotationComponentView.requiredMessage().isEmpty()) ? annotationComponentView.requiredMessage() : "El campo '" + params.getNameComponentView() + "' debe ser obligatorio";
        
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(nameProperty, params.getTypeComponentView());
        // Obtenemos el metodo get y set del atributo del modelo o propiedad en el controlador
        
        if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.MODEL)) {
            Method methodGetFieldView = propertyDescriptor.getReadMethod();
            
            Object value = methodGetFieldView.invoke(params.getObjectComponentView());
            params.getMethodSetFieldModel().invoke(params.getObjectModelBean(), value);
            
            if (value == null && required) {
                NotifyUtils.showErrorAutoHide(null, "Valor Requerido", requiredMessage);
                //this.failedValidation = true;
                params.setFailedValidation(true);
            }
        }
        
        if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.VIEW)) {
            Method methodSetFieldView = propertyDescriptor.getWriteMethod();
            //model.setSelectedItem(fieldModel.get(objectModelBean));
            methodSetFieldView.invoke(params.getObjectComponentView(), params.getMethodGetFieldModel().invoke(params.getObjectModelBean()));
        }
        
//        for (Method m : params.getTypeComponentView().getMethods()) {
//            if (m.getName().equals("get" + StringUtils.capitalize(nameProperty))) {
//                if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.MODEL)) {
//                    //fieldModel.set(objectModelBean, m.invoke(objectComponentView));
//                    params.getMethodSetFieldModel().invoke(params.getObjectModelBean(), m.invoke(params.getObjectComponentView()));
//                }
//            }
//
//            if (m.getName().equals("set" + StringUtils.capitalize(nameProperty))) {
//                if (Objects.equals(params.getTipoUpdateEnum(), TipoUpdateEnum.VIEW)) {
//                    //m.invoke(objectComponentView, fieldModel.get(objectModelBean));
//                    m.invoke(params.getObjectComponentView(), params.getMethodGetFieldModel().invoke(params.getObjectModelBean()));
//                }
//            }
//        }
    }
    
}
