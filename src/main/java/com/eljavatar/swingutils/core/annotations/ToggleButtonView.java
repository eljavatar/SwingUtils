package com.eljavatar.swingutils.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta Anotacion es usada para demarcar componentes java swing que extienden 
 * de la clase javax.swing.JToggleButton
 * <p>
 * Componentes en los que puede ser usado:
 * <ul>
 * <li>
 * {@link javax.swing.JRadioButton}
 * <li>
 * {@link javax.swing.JCheckBox}
 * <li>
 * {@link javax.swing.JToggleButton}
 * </ul>
 * 
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ToggleButtonView {
    
    String name();
    
}
