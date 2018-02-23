package com.eljavatar.swingutils.core.annotations;

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
    
}
