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
package com.eljavatar.swingutils.core.annotations;

import com.eljavatar.swingutils.util.DigestEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordTextView {
    
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
     * Indica si el valor a ingresar en el campo de texto es requerido o no; en caso de ser
     * <code>true</code>, se mostrará un mensaje de error, el cual puede ser personalizado
     * mediante la propiedad <code>requiredMessage</code>
     * @return Valor bboleano que indica si el valor es obligatorio o no
     */
    boolean required() default false;
    
    /**
     * <p>
     * Mediante esta propiedad es posible personalizar el mensaje de error que se muestra
     * cuando la propiedad <code>required</code> se marca como <code>true</code>, pero no
     * se ingresa ningún valor en el campo de texto
     * @return Mensaje de error personalizado cuando required is true
     */
    String requiredMessage() default "";
    
    /**
     * 
     * @return 
     */
    boolean getCharArray() default true;
    
    /**
     * 
     * @return Algoritmo de encriptación que se usará para la contraseña
     */
    DigestEnum digest() default DigestEnum.EMPTY;
    
}
