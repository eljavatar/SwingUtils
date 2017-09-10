package com.eljavatar.swingutils.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta Anotacion es usada para demarcar componentes java swing que extienden 
 * de la clase javax.swing.JTextComponent y que serán usadas para texto de
 * proposito general
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
public @interface TextView {
    
    String name();
    
}
