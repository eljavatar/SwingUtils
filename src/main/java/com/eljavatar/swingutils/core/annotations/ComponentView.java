/*
 * Copyright 2018 Andres.
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
package com.eljavatar.swingutils.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta Anotacion puede ser usada en cualquier tipo de componente
 * Lo único que requiere esta anotación, es el nombre de la propiedad con la cual
 * se accede al valor que se desea obtener o modificar
 * 
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentView {
    
    /**
     * <p>
     * Indica el nombre de la propiedad en el controlador para sincronizar los valores en la vista
     * y en el controlador
     * <p>
     * Cuando la propiedad en el controlador es un <code>@ModelBean</code>, <code>name</code> debe tener
     * la siguiente estructura: objetoModelo.atributo; Cuando la propiedad en el controlador es un
     * <code>@PropertyController</code>, <code>name</code> debe llamarse como está en el controlador
     * @return Nombre de la propiedad en el controlador
     */
    String name();
    
    /**
     * <p>
     * Normalmente, muchos componentes personalizados devuelven un valor esperado por el usuario
     * o asignan uno ingresado oor éste a través de sus correspondientes métodos 'get' o 'set'
     * respectivamente; Por lo tanto, si los metodos son por ejemplo getMiValueObject y setMiValueObject,
     * <code>nameProperty</code> debe ser entonces miValueObject
     * @return Nombre de la propiedad para acceder al valor requerido mediante su metodo 'get' o 'set'
     */
    String nameProperty();
    
}
