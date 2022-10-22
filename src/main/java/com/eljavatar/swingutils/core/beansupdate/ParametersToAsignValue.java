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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ParametersToAsignValue {
    
    private Field fieldView;
    private String nameComponentView;
    private Class typeComponentView;
    private TipoUpdateEnum tipoUpdateEnum;
    private Field fieldModel;
    private Class typeFieldModel;
    
    private Object objectModelBean;
    private Object objectComponentView;
    private Method methodSetFieldModel;
    private Method methodGetFieldModel;
    
    private boolean failedValidation;

    public ParametersToAsignValue(Field fieldView, String nameComponentView, Class typeComponentView, TipoUpdateEnum tipoUpdateEnum, Field fieldModel, Class typeFieldModel, Object objectModelBean, Object objectComponentView, Method methodSetFieldModel, Method methodGetFieldModel, boolean failedValidation) {
        this.fieldView = fieldView;
        this.nameComponentView = nameComponentView;
        this.typeComponentView = typeComponentView;
        this.tipoUpdateEnum = tipoUpdateEnum;
        this.fieldModel = fieldModel;
        this.typeFieldModel = typeFieldModel;
        this.objectModelBean = objectModelBean;
        this.objectComponentView = objectComponentView;
        this.methodSetFieldModel = methodSetFieldModel;
        this.methodGetFieldModel = methodGetFieldModel;
        this.failedValidation = failedValidation;
    }

    public ParametersToAsignValue() {
    }
    
    

    public Field getFieldView() {
        return fieldView;
    }

    public void setFieldView(Field fieldView) {
        this.fieldView = fieldView;
    }

    public String getNameComponentView() {
        return nameComponentView;
    }

    public void setNameComponentView(String nameComponentView) {
        this.nameComponentView = nameComponentView;
    }

    public Class getTypeComponentView() {
        return typeComponentView;
    }

    public void setTypeComponentView(Class typeComponentView) {
        this.typeComponentView = typeComponentView;
    }

    public TipoUpdateEnum getTipoUpdateEnum() {
        return tipoUpdateEnum;
    }

    public void setTipoUpdateEnum(TipoUpdateEnum tipoUpdateEnum) {
        this.tipoUpdateEnum = tipoUpdateEnum;
    }

    public Field getFieldModel() {
        return fieldModel;
    }

    public void setFieldModel(Field fieldModel) {
        this.fieldModel = fieldModel;
    }

    public Class getTypeFieldModel() {
        return typeFieldModel;
    }

    public void setTypeFieldModel(Class typeFieldModel) {
        this.typeFieldModel = typeFieldModel;
    }

    
    
    
    
    public Object getObjectModelBean() {
        return objectModelBean;
    }

    public void setObjectModelBean(Object objectModelBean) {
        this.objectModelBean = objectModelBean;
    }
    
    public Object getObjectComponentView() {
        return objectComponentView;
    }

    public void setObjectComponentView(Object objectComponentView) {
        this.objectComponentView = objectComponentView;
    }
    
    public Method getMethodSetFieldModel() {
        return methodSetFieldModel;
    }

    public void setMethodSetFieldModel(Method methodSetFieldModel) {
        this.methodSetFieldModel = methodSetFieldModel;
    }

    public Method getMethodGetFieldModel() {
        return methodGetFieldModel;
    }

    public void setMethodGetFieldModel(Method methodGetFieldModel) {
        this.methodGetFieldModel = methodGetFieldModel;
    }

    public boolean isFailedValidation() {
        return failedValidation;
    }

    public void setFailedValidation(boolean failedValidation) {
        this.failedValidation = failedValidation;
    }
    
}
