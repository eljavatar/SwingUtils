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

import com.eljavatar.swingutils.core.annotations.NumberTextView;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class NumberTextViewStrategy extends AbstractTextComponentView implements ComponentAnnotatedViewStrategy {

    @Override
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        NumberTextView annotationNumberTextView = params.getFieldView().getAnnotation(NumberTextView.class);
        String pattern = annotationNumberTextView.pattern();
        Locale locale = annotationNumberTextView.locale() != null ? annotationNumberTextView.locale().getLocale() : null;
        boolean required = annotationNumberTextView.required();
        String requiredMessage = (annotationNumberTextView.requiredMessage() != null && !annotationNumberTextView.requiredMessage().isEmpty()) ? annotationNumberTextView.requiredMessage() : "El campo '" + params.getNameComponentView() + "' debe contener un valor";
        
        updateGenericTextViewData(params.getTypeComponentView(), params.getObjectComponentView(), params.getNameComponentView(), params.getFieldModel(), params.getTypeFieldModel(), params.getMethodSetFieldModel(), params.getMethodGetFieldModel(), params.getObjectModelBean(), params.getTipoUpdateEnum(), pattern, locale, required, requiredMessage, null);
        
        params.setFailedValidation(isFailedValidation());
    }
    
}
