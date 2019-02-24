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

import com.eljavatar.swingutils.util.LocaleEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta Anotacion es usada para demarcar componentes java swing que extienden 
 * de la clase javax.swing.JTextComponent y que serán usadas para texto de
 * tipo numérico
 * <p>
 * Componentes en los que puede ser usado:
 * <ul>
 * <li>
 * {@link javax.swing.JTextField}
 * <li>
 * {@link javax.swing.JFormattedTextField}
 * <li>
 * {@link javax.swing.JPasswordField}
 * <li>
 * {@link javax.swing.JTextArea}
 * <li>
 * {@link javax.swing.JEditorPane}
 * <li>
 * {@link javax.swing.JTextPane}
 * </ul>
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberTextView {
    
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
     * 
     * @return Pattern de número
     */
    String pattern() default "";
    
    /**
     * <p>
     * Indica la configuración regional que se usará para especificar el formato de los números
     * @return Locale para aplicar al formato del número
     */
    LocaleEnum locale() default LocaleEnum.DEFAULT;
    
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
    
}
