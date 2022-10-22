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

import com.eljavatar.swingutils.core.annotations.TextView;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class TextViewStrategy extends AbstractTextComponentView implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        TextView annotationTextView = params.getFieldView().getAnnotation(TextView.class);
        boolean required = annotationTextView.required();
        String requiredMessage = (annotationTextView.requiredMessage() != null && !annotationTextView.requiredMessage().isEmpty()) ? annotationTextView.requiredMessage() : "El campo '" + params.getNameComponentView() + "' debe contener un valor";
        
        updateGenericTextViewData(params.getTypeComponentView(), params.getObjectComponentView(), params.getNameComponentView(), params.getFieldModel(), params.getTypeFieldModel(), params.getMethodSetFieldModel(), params.getMethodGetFieldModel(), params.getObjectModelBean(), params.getTipoUpdateEnum(), null, null, required, requiredMessage, null);
        
        params.setFailedValidation(isFailedValidation());
    }
    
}
