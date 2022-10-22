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

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ComponentAnnotatedViewContext {
    
    private final ComponentAnnotatedViewStrategy strategy;

    public ComponentAnnotatedViewContext(ComponentAnnotatedViewStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void asignValue(ParametersToAsignValue params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        strategy.asignValue(params);
    }
    
}
