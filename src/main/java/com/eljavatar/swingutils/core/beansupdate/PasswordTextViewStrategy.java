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

import com.eljavatar.swingutils.core.annotations.PasswordTextView;
import com.eljavatar.swingutils.util.DigestEnum;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class PasswordTextViewStrategy extends AbstractTextComponentView implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        PasswordTextView annotationPasswordTextView = params.getFieldView().getAnnotation(PasswordTextView.class);
        boolean required = annotationPasswordTextView.required();
        String requiredMessage = (annotationPasswordTextView.requiredMessage() != null && !annotationPasswordTextView.requiredMessage().isEmpty()) ? annotationPasswordTextView.requiredMessage() : "El campo '" + params.getNameComponentView() + "' debe contener un valor";
        DigestEnum digest = annotationPasswordTextView.digest() != null ? annotationPasswordTextView.digest() : DigestEnum.EMPTY;
        
        updateGenericTextViewData(params.getTypeComponentView(), params.getObjectComponentView(), params.getNameComponentView(), params.getFieldModel(), params.getTypeFieldModel(), params.getMethodSetFieldModel(), params.getMethodGetFieldModel(), params.getObjectModelBean(), params.getTipoUpdateEnum(), null, null, required, requiredMessage, digest);
        
        params.setFailedValidation(isFailedValidation());
    }
    
}
